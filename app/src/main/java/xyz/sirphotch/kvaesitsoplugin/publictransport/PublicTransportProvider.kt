package xyz.sirphotch.kvaesitsoplugin.publictransport

import android.graphics.Color
import android.os.Debug
import de.mm20.launcher2.plugin.config.QueryPluginConfig
import de.mm20.launcher2.plugin.config.StorageStrategy
import de.mm20.launcher2.sdk.base.GetParams
import de.mm20.launcher2.sdk.base.SearchParams
import de.mm20.launcher2.sdk.locations.Location
import de.mm20.launcher2.sdk.locations.LocationProvider
import de.mm20.launcher2.sdk.locations.LocationQuery
import de.mm20.launcher2.search.location.Address
import de.mm20.launcher2.search.location.Departure
import de.mm20.launcher2.search.location.LineType
import de.mm20.launcher2.search.location.LocationIcon
import de.schildbach.pte.BayernProvider
import de.schildbach.pte.dto.LocationType
import de.schildbach.pte.dto.Point
import de.schildbach.pte.dto.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

import de.schildbach.pte.dto.Location as PteLocation
import de.schildbach.pte.dto.Departure as PteDeparture

class PublicTransportProvider : LocationProvider(
    config = QueryPluginConfig(
        storageStrategy = StorageStrategy.Deferred
    )
) {
    private val bayernProvider: BayernProvider = BayernProvider()

    override suspend fun search(query: LocationQuery, params: SearchParams): List<Location> {
        if (!params.allowNetwork || query.query.length < 3)
            return emptyList()

        return withContext(Dispatchers.IO) {
            // wait for debugger only in debug mode
            // Debug.waitForDebugger()

            bayernProvider.queryNearbyLocations(
                setOf(LocationType.STATION),
                PteLocation.coord(Point.fromDouble(query.userLatitude, query.userLongitude)),
                query.searchRadius.toInt(),
                0
            ).locations
                .filter { it.name?.contains(query.query, ignoreCase = true) ?: false }
                .associateWith {
                    bayernProvider.queryDepartures(
                        it.id, null, 7, false
                    ).stationDepartures.flatMap { it.departures }
                }.mapNotNull { it.toLauncherLocation() }
        }
    }

    override suspend fun get(
        id: String,
        params: GetParams
    ): Location? = bayernProvider
        .queryDepartures(id, null, 7, false)
        .stationDepartures
        .associate { it.location to it.departures }
        .map { it.toLauncherLocation() }
        .firstOrNull()


    private fun Map.Entry<PteLocation, List<PteDeparture>>.toLauncherLocation(): Location? {
        val (location, departures) = this
        val (category, icon) = location.products?.toCategory() ?: (null to null)
        return Location(
            id = location.id ?: return null,
            label = location.name ?: return null,
            latitude = location.latAsDouble,
            longitude = location.lonAsDouble,
            icon = icon,
            category = category,
            address = Address(location.place),
            departures = departures.mapNotNull { d ->
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

}