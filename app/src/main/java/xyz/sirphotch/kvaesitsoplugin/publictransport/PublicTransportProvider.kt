package xyz.sirphotch.kvaesitsoplugin.publictransport

import android.graphics.Color
import android.util.Log
import de.mm20.launcher2.plugin.config.QueryPluginConfig
import de.mm20.launcher2.plugin.config.StorageStrategy
import de.mm20.launcher2.sdk.base.GetParams
import de.mm20.launcher2.sdk.base.RefreshParams
import de.mm20.launcher2.sdk.base.SearchParams
import de.mm20.launcher2.sdk.locations.Location
import de.mm20.launcher2.sdk.locations.LocationProvider
import de.mm20.launcher2.sdk.locations.LocationQuery
import de.mm20.launcher2.search.location.Address
import de.mm20.launcher2.search.location.Attribution
import de.mm20.launcher2.search.location.Departure
import de.mm20.launcher2.search.location.LineType
import de.mm20.launcher2.search.location.LocationIcon
import de.schildbach.pte.NetworkProvider
import de.schildbach.pte.dto.LocationType
import de.schildbach.pte.dto.Point
import de.schildbach.pte.dto.Product
import de.schildbach.pte.dto.QueryDeparturesResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import xyz.sirphotch.kvaesitsoplugin.publictransport.data.dataStore
import xyz.sirphotch.kvaesitsoplugin.publictransport.providers.NetworkProviderFactory
import xyz.sirphotch.kvaesitsoplugin.publictransport.providers.Provider
import java.io.IOException
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

import de.schildbach.pte.dto.Location as PteLocation
import de.schildbach.pte.dto.Departure as PteDeparture
import android.location.Location as AndroidLocation

class PublicTransportProvider : LocationProvider(
    config = QueryPluginConfig(
        storageStrategy = StorageStrategy.StoreCopy
    )
) {
    private lateinit var enabledProviders: Flow<Set<Provider>>

    override fun onCreate(): Boolean {
        enabledProviders = context!!.applicationContext.dataStore.data.map { it.enabledProviders ?: emptySet() }
        return super.onCreate()
    }

    override suspend fun search(query: LocationQuery, params: SearchParams): List<Location> {
        if (!params.allowNetwork || query.query.length < 3)
            return emptyList()

        return withContext(Dispatchers.IO) {
            val enabledProviders = enabledProviders.firstOrNull() ?: return@withContext emptyList()

            fun NetworkProvider.nearbyLocations(): List<PteLocation> =
                runCatching {
                    queryNearbyLocations(
                        setOf(LocationType.STATION),
                        PteLocation.coord(
                            Point.fromDouble(
                                query.userLatitude,
                                query.userLongitude
                            )
                        ),
                        query.searchRadius.toInt(),
                        0
                    ).locations.filter {
                        it.name?.contains(query.query, ignoreCase = true) ?: false
                    }
                }.onFailure {
                    Log.e("Sir-Photch", "${id().name}: queryNearbyLocations", it)
                }.getOrDefault(emptyList())

            fun NetworkProvider.suggest(): List<PteLocation> =
                runCatching {
                    suggestLocations(query.query, setOf(LocationType.STATION), 0).locations.filter {
                        it.distanceTo(query.userLatitude, query.userLongitude) < query.searchRadius
                    }
                }.onFailure {
                    Log.e("Sir-Photch", "${id().name} suggestLocations", it)
                }.getOrDefault(emptyList())


            enabledProviders.map { id ->
                with(NetworkProviderFactory.get(id)) {
                    async {
                        when {
                            !hasCapabilities(NetworkProvider.Capability.DEPARTURES) -> return@async emptyList()
                            hasCapabilities(NetworkProvider.Capability.NEARBY_LOCATIONS) -> nearbyLocations()
                            hasCapabilities(NetworkProvider.Capability.SUGGEST_LOCATIONS) -> suggest()
                            else -> return@async emptyList()
                        }.associateWith {
                            runCatching {
                                queryDepartures(
                                    it.id,
                                    null,
                                    7,
                                    false
                                ).stationDepartures.flatMap { it.departures }
                            }.onFailure {
                                Log.e("Sir-Photch", "${id().name} queryDepartures", it)
                            }.getOrDefault(emptyList())
                        }.mapNotNull { it.toLauncherLocation(id) }
                    }
                }
            }.awaitAll().flatten()
        }
    }

    override suspend fun refresh(item: Location, params: RefreshParams): Location? {
        val (provStr, id) = item.id.split('/', limit = 2)
        val provider =
            provStr.runCatching { Provider.valueOf(this) }.getOrNull() ?: return null

        val availableProviders = enabledProviders.firstOrNull() ?: return null
        if (!availableProviders.contains(provider)) return null

        with(NetworkProviderFactory.get(provider)) {
            if (!hasCapabilities(NetworkProvider.Capability.DEPARTURES)) return null

            val queryResult = try {
                withContext(Dispatchers.IO) {
                    queryDepartures(
                        id,
                        null,
                        7,
                        false
                    )
                }
            } catch (ioe: IOException) {
                Log.e("Sir-Photch", "${id().name} queryDepartures", ioe)
                return item
            }

            if (QueryDeparturesResult.Status.INVALID_STATION == queryResult.status)
                return null

            if (QueryDeparturesResult.Status.SERVICE_DOWN == queryResult.status)
                return item

            val refreshedPteLocations =
                queryResult.stationDepartures.associate { it.location to it.departures }

            if (1 < refreshedPteLocations.size) {
                Log.w("Sir-Photch", "${id().name} ignoring multiple results for refresh")
            }

            val (loc, deps) =
                refreshedPteLocations.entries.firstOrNull() ?: run {
                    Log.e(
                        "Sir-Photch",
                        "${id().name} no results on successful query; Header: ${queryResult.header}"
                    )
                    return item
                }

            val (category, icon) = loc.products?.toCategory() ?: (null to null)

            return item.copy(
                label = loc.name ?: item.label,
                latitude = if (loc.hasCoord()) loc.latAsDouble else item.latitude,
                longitude = if (loc.hasCoord()) loc.lonAsDouble else item.longitude,
                icon = icon ?: item.icon,
                category = category ?: item.category,
                departures = deps.toDepartures()
            )
        }
    }

    private fun Map.Entry<PteLocation, List<PteDeparture>>.toLauncherLocation(
        provider: Provider
    ): Location? {
        val (location, departures) = this
        val (category, icon) = location.products?.toCategory() ?: (null to null)
        return Location(
            id = provider.name + '/' + (location.id ?: return null),
            label = location.name ?: "",
            latitude = location.latAsDouble,
            longitude = location.lonAsDouble,
            icon = icon,
            category = category,
            attribution = Attribution(
                provider.name,
                iconUrl = null,
                url = null,
            ),
            departures = departures.toDepartures(),
        )
    }

    private fun List<PteDeparture>.toDepartures(): List<Departure> = this.mapNotNull { d ->
        val departureTime = d.plannedTime?.toInstant()
            ?.let { ZonedDateTime.ofInstant(it, ZoneId.systemDefault()) }
            ?: return@mapNotNull null
        val delay = d.predictedTime?.toInstant()
            ?.let { Duration.between(d.plannedTime!!.toInstant(), it) }
        Departure(
            time = departureTime,
            delay = delay,
            line = d.line.label ?: d.line.name ?: return@mapNotNull null,
            lastStop = d.destination?.uniqueShortName(),
            type = d.line.product?.toLineType(),
            lineColor = d.line.style?.let { Color.valueOf(it.backgroundColor) }
        )
    }

    private fun Set<Product>.toCategory(): Pair<String, LocationIcon> = when {
        contains(Product.HIGH_SPEED_TRAIN) || contains(Product.REGIONAL_TRAIN) || contains(Product.SUBURBAN_TRAIN) -> "Bahnhof" to LocationIcon.Train
        contains(Product.SUBWAY) -> "U-Bahnhof" to LocationIcon.Subway
        contains(Product.TRAM) -> "Straßenbahnhaltestelle" to LocationIcon.Tram
        contains(Product.BUS) -> "Bushaltestelle" to LocationIcon.Bus
        else -> "Haltestelle" to LocationIcon.GenericTransit
    }

    private fun Product.toLineType(): LineType? = when (this) {
        Product.HIGH_SPEED_TRAIN -> LineType.HighSpeedTrain
        Product.REGIONAL_TRAIN -> LineType.RegionalTrain
        Product.SUBURBAN_TRAIN -> LineType.CommuterTrain
        Product.SUBWAY -> LineType.Subway
        Product.TRAM -> LineType.Tram
        Product.CABLECAR -> LineType.CableCar
        Product.FERRY -> LineType.Boat
        Product.BUS -> LineType.Bus
        Product.ON_DEMAND -> null
    }

    private fun PteLocation.distanceTo(lat: Double, lon: Double): Float =
        AndroidLocation("KvaesitsoLocationProvider").apply {
            latitude = lat
            longitude = lon
        }.distanceTo(
            AndroidLocation("PublicTransportProvider").apply {
                latitude = this@distanceTo.latAsDouble
                longitude = this@distanceTo.lonAsDouble
            }
        )
}
