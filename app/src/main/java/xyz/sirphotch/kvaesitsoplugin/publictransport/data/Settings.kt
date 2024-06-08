package xyz.sirphotch.kvaesitsoplugin.publictransport.data

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import xyz.sirphotch.kvaesitsoplugin.publictransport.providers.Provider
import java.io.InputStream
import java.io.OutputStream

private const val SETTINGS_VERSION = 1

@Serializable
data class Settings(
    val version: Int = SETTINGS_VERSION,
    val enabledProviders: Set<Provider>? = null,
)

val Context.dataStore by dataStore("settings.json", SettingsSerializer)

@OptIn(ExperimentalSerializationApi::class)
object SettingsSerializer: Serializer<Settings> {
    private val lenientJson = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
        coerceInputValues = true
    }

    override val defaultValue: Settings = Settings()

    override suspend fun readFrom(input: InputStream): Settings {
        val settings = lenientJson.decodeFromStream<Settings>(input)

        if (settings.version < SETTINGS_VERSION) {
            // TODO
        }

        return settings
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) {
        lenientJson.encodeToStream(Settings.serializer(), t, output)
    }
}