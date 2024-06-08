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

@Serializable
data class Settings(
    val enabledProviders: Set<Provider>? = null,
    val deduplicateResults: Boolean = false,
)

val Context.dataStore by dataStore("settings.json", SettingsSerializer)

@OptIn(ExperimentalSerializationApi::class)
object SettingsSerializer: Serializer<Settings> {
    override val defaultValue: Settings = Settings()

    override suspend fun readFrom(input: InputStream): Settings {
        return Json.decodeFromStream(input)
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) {
        Json.encodeToStream(Settings.serializer(), t, output)
    }
}