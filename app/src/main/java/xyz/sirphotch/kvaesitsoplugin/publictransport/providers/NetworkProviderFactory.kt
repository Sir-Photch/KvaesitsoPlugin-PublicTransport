package xyz.sirphotch.kvaesitsoplugin.publictransport.providers

import de.schildbach.pte.BayernProvider
import de.schildbach.pte.BsvagProvider
import de.schildbach.pte.DingProvider
import de.schildbach.pte.DubProvider
import de.schildbach.pte.EireannProvider
import de.schildbach.pte.GvhProvider
import de.schildbach.pte.KvvProvider
import de.schildbach.pte.LinzProvider
import de.schildbach.pte.MerseyProvider
import de.schildbach.pte.MvgProvider
import de.schildbach.pte.MvvProvider
import de.schildbach.pte.NegentweeProvider
import de.schildbach.pte.NetworkProvider
import de.schildbach.pte.NsProvider
import de.schildbach.pte.NvbwProvider
import de.schildbach.pte.RtProvider
import de.schildbach.pte.RtaChicagoProvider
import de.schildbach.pte.StvProvider
import de.schildbach.pte.SydneyProvider
import de.schildbach.pte.TlemProvider
import de.schildbach.pte.VblProvider
import de.schildbach.pte.VgnProvider
import de.schildbach.pte.VmvProvider
import de.schildbach.pte.VrnProvider
import de.schildbach.pte.VrrProvider
import de.schildbach.pte.VvmProvider
import de.schildbach.pte.VvoProvider
import de.schildbach.pte.VvsProvider
import de.schildbach.pte.VvvProvider
import de.schildbach.pte.WienProvider
import java.util.Collections

object NetworkProviderFactory {
    private val providerInstances: MutableMap<Provider, NetworkProvider> = Collections.synchronizedMap(mutableMapOf())

    fun get(provider: Provider): NetworkProvider =
        providerInstances.getOrPut(
            provider
        ) {
            when (provider) {
                Provider.Bayern -> BayernProvider()
                Provider.Bsvag -> BsvagProvider()
                Provider.Ding -> DingProvider()
                Provider.Dub -> DubProvider()
                Provider.Gvh -> GvhProvider()
                Provider.Kvv -> KvvProvider()
                Provider.Linz -> LinzProvider()
                Provider.Mersey -> MerseyProvider()
                Provider.Mvg -> MvgProvider()
                Provider.Mvv -> MvvProvider()
                Provider.Nvbw -> NvbwProvider()
                Provider.RtaChicago -> RtaChicagoProvider()
                Provider.Stv -> StvProvider()
                Provider.Sydney -> SydneyProvider()
                Provider.Tlem -> TlemProvider()
                Provider.Vbl -> VblProvider()
                Provider.Vgn -> VgnProvider()
                Provider.Vmv -> VmvProvider()
                Provider.Vrn -> VrnProvider()
                Provider.Vrr -> VrrProvider()
                Provider.Vvm -> VvmProvider()
                Provider.Vvo -> VvoProvider()
                Provider.Vvs -> VvsProvider()
                Provider.Vvv -> VvvProvider()
                Provider.Wien -> WienProvider()
                Provider.Eireann -> EireannProvider()
                Provider.Ns -> NsProvider()
                Provider.Rt -> RtProvider()
                Provider.Negentwee -> NegentweeProvider()
            }
        }

}