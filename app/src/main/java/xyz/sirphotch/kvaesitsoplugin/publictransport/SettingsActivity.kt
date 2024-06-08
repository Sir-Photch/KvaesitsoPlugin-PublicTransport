package xyz.sirphotch.kvaesitsoplugin.publictransport

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import xyz.sirphotch.kvaesitsoplugin.publictransport.data.Settings
import xyz.sirphotch.kvaesitsoplugin.publictransport.data.dataStore
import xyz.sirphotch.kvaesitsoplugin.publictransport.providers.Provider
import xyz.sirphotch.kvaesitsoplugin.publictransport.ui.theme.KvaesitsoPublicTransportPluginTheme

class SettingsActivity : ComponentActivity() {

    private val updateSettingsScope = CoroutineScope(Job() + Dispatchers.Default)
    private fun updateSettings(f: (Settings) -> Settings) {
        updateSettingsScope.launch {
            dataStore.updateData { f(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
        enableEdgeToEdge()
        setContent {
            window.navigationBarColor = MaterialTheme.colorScheme.surface.toArgb()
            KvaesitsoPublicTransportPluginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProviderGroupColumn(
                        dataStore.data,
                        updateSettings = { updateSettings(it) },
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProviderGroupColumn(
    settings: Flow<Settings>,
    updateSettings: ((Settings) -> Settings) -> Unit,
    modifier: Modifier = Modifier
) {
    val enabledProviders by settings.map { it.enabledProviders }.collectAsState(null)

    Column(modifier.background(MaterialTheme.colorScheme.surface)) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .statusBarsPadding()
        )
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = MaterialTheme.shapes.extraLarge.copy(
                bottomEnd = CornerSize(0), bottomStart = CornerSize(0)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    for ((region, providers) in Provider.entries.groupBy { it.region() }) {
                        var showRegionProviders by remember { mutableStateOf(false) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .clickable {
                                    showRegionProviders = !showRegionProviders
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(region.localizedName()),
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            Icon(
                                Icons.Rounded.ArrowDropDown, null,
                                modifier = Modifier.graphicsLayer {
                                    rotationZ = if (showRegionProviders) 180f else 0f
                                }
                            )
                        }
                        AnimatedVisibility(showRegionProviders) {
                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                for (provider in providers) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 3.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            stringResource(provider.localizedName()) + (stringResource(
                                                provider.localizedShortName()
                                            ).takeIf { it.isNotBlank() }?.let { " ($it)" } ?: ""),
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier
                                                .fillMaxWidth(0.66f)
                                                .basicMarquee()
                                        )
                                        Switch(
                                            enabledProviders?.contains(provider) == true,
                                            onCheckedChange = { checked ->
                                                updateSettings {
                                                    if (checked) {
                                                        it.copy(
                                                            enabledProviders = it.enabledProviders.orEmpty() + provider
                                                        )
                                                    } else {
                                                        it.copy(
                                                            enabledProviders = it.enabledProviders?.minus(
                                                                provider
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun GreetingPreview() {
    val mockSettings = Settings()

    KvaesitsoPublicTransportPluginTheme {
        ProviderGroupColumn(
            flow { emit(mockSettings) },
            updateSettings = { it(mockSettings) }
        )
    }
}