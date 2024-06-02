package xyz.sirphotch.kvaesitsoplugin.publictransport

import de.mm20.launcher2.plugin.config.QueryPluginConfig
import de.mm20.launcher2.plugin.config.StorageStrategy
import de.mm20.launcher2.sdk.base.GetParams
import de.mm20.launcher2.sdk.base.SearchParams
import de.mm20.launcher2.sdk.locations.Location
import de.mm20.launcher2.sdk.locations.LocationProvider
import de.mm20.launcher2.sdk.locations.LocationQuery

class PublicTransportProvider : LocationProvider(
    config = QueryPluginConfig(
        storageStrategy = StorageStrategy.Deferred
    )
)  {
    override suspend fun search(query: LocationQuery, params: SearchParams): List<Location> {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String, params: GetParams): Location? {
        return super.get(id, params)
    }


}