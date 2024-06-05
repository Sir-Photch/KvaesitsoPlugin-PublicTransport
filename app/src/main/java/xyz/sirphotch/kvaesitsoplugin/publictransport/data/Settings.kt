package xyz.sirphotch.kvaesitsoplugin.publictransport.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import xyz.sirphotch.kvaesitsoplugin.publictransport.providers.Provider

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Settings {

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    private val enabledProviders = stringSetPreferencesKey("enabledProviders")
    fun enabledProviders(context: Context): Flow<Set<Provider>> =
        context.dataStore.data.mapNotNull {
            it[enabledProviders]?.mapNotNull {
                it.runCatching { Provider.valueOf(this) }.getOrNull()
            }?.toSet()
        }

    fun getProviderEnabled(context: Context, provider: Provider): Flow<Boolean> =
        context.dataStore.data.mapNotNull {
            it[enabledProviders]?.contains(provider.name)
        }

    fun setProviderEnabled(context: Context, provider: Provider, enabled: Boolean) {
        scope.launch {
            context.dataStore.edit {
                val current = it[enabledProviders]

                when {
                    current.isNullOrEmpty() && !enabled -> return@edit
                    current.isNullOrEmpty() && enabled -> it[enabledProviders] = setOf(provider.name)
                    enabled -> it[enabledProviders] = current!! + provider.name
                    !enabled -> it[enabledProviders] = current!! - provider.name
                }
            }
        }
    }

}