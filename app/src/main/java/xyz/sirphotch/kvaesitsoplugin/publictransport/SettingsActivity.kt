package xyz.sirphotch.kvaesitsoplugin.publictransport

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.pearx.kasechange.toTitleCase
import xyz.sirphotch.kvaesitsoplugin.publictransport.data.Settings
import xyz.sirphotch.kvaesitsoplugin.publictransport.providers.Provider
import xyz.sirphotch.kvaesitsoplugin.publictransport.ui.theme.KvaesitsoPublicTransportPluginTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            KvaesitsoPublicTransportPluginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProviderGroupColumn(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProviderGroupColumn(modifier: Modifier = Modifier) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        for ((region, providers) in Provider.entries.groupBy { it.region() }) {
            var showRegionProviders by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clickable {
                        showRegionProviders = !showRegionProviders
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    region.name.toTitleCase(),
                    style = MaterialTheme.typography.titleLarge,
                )
                Icon(Icons.Rounded.ArrowDropDown, null)
            }
            AnimatedVisibility(showRegionProviders) {

                val context = LocalContext.current

                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    for (provider in providers) {
                        val providerEnabled by Settings.getProviderEnabled(context, provider)
                            .collectAsState(false)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                provider.localizedName(),
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier
                                    .fillMaxWidth(0.66f)
                                    .basicMarquee()
                            )
                            Switch(
                                providerEnabled,
                                onCheckedChange = {
                                    Settings.setProviderEnabled(
                                        context,
                                        provider,
                                        !providerEnabled
                                    )
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

@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun GreetingPreview() {
    KvaesitsoPublicTransportPluginTheme {
        ProviderGroupColumn()
    }
}