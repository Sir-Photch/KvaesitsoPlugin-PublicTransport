package xyz.sirphotch.kvaesitsoplugin.publictransport.providers

import android.net.Uri
import xyz.sirphotch.kvaesitsoplugin.publictransport.R

enum class ProviderRegion {
    Germany,
    UnitedArabEmirates,
    Austria,
    UnitedKingdom,
    UnitedStates,
    Australia,
    Switzerland,
    Ireland,
    Netherlands,
    Europe;

    fun localizedName(): Int = when (this) {
        Germany -> R.string.region_germany
        UnitedArabEmirates -> R.string.region_united_arab_emirates
        Austria -> R.string.region_austria
        UnitedKingdom -> R.string.region_united_kingdom
        UnitedStates -> R.string.region_united_states
        Australia -> R.string.region_australia
        Switzerland -> R.string.region_switzerland
        Ireland -> R.string.region_ireland
        Netherlands -> R.string.region_netherlands
        Europe -> R.string.region_europe
    }
}

enum class Provider {
    // EfaProvider
    Bayern,
    Bsvag,
    Ding,
    Dub,
    Gvh,
    Kvv,
    Linz,
    Mersey,
    Mvg,
    Mvv,
    Nvbw,
    RtaChicago,
    Stv,
    Sydney,
    Tlem,
    Vbl,
    Vgn,
    Vmv,
    Vrn,
    Vrr,
    Vvm,
    Vvo,
    Vvs,
    Vvv,
    Wien,

    // HafasLegacyProvider
    Eireann,
    Ns,
    Rt,

    // AbstractNetworkProvider
    Negentwee;

    fun localizedShortName(): Int = when (this) {
        Bayern -> R.string.provider_bayern_short
        Bsvag -> R.string.provider_bsvag_short
        Ding -> R.string.provider_ding_short
        Dub -> R.string.provider_dub_short
        Gvh -> R.string.provider_gvh_short
        Kvv -> R.string.provider_kvv_short
        Linz -> R.string.provider_linz_short
        Mersey -> R.string.provider_mersey_short
        Mvg -> R.string.provider_mvg_short
        Mvv -> R.string.provider_mvv_short
        Nvbw -> R.string.provider_nvbw_short
        RtaChicago -> R.string.provider_rta_chicago_short
        Stv -> R.string.provider_stv_short
        Sydney -> R.string.provider_sydney_short
        Tlem -> R.string.provider_tlem_short
        Vbl -> R.string.provider_vbl_short
        Vgn -> R.string.provider_vgn_short
        Vmv -> R.string.provider_vmv_short
        Vrn -> R.string.provider_vrn_short
        Vrr -> R.string.provider_vrr_short
        Vvm -> R.string.provider_vvm_short
        Vvo -> R.string.provider_vvo_short
        Vvs -> R.string.provider_vvs_short
        Vvv -> R.string.provider_vvv_short
        Wien -> R.string.provider_wien_short
        Eireann -> R.string.provider_eireann_short
        Ns -> R.string.provider_ns_short
        Rt -> R.string.provider_rt_short
        Negentwee -> R.string.provider_negentwee_short
    }

    fun localizedName(): Int = when (this) {
        Bsvag -> R.string.provider_bsvag
        Ding -> R.string.provider_ding
        Dub -> R.string.provider_dub
        Gvh -> R.string.provider_gvh
        Kvv -> R.string.provider_kvv
        Mersey -> R.string.provider_mersey
        Mvg -> R.string.provider_mvg
        Mvv -> R.string.provider_mvv
        Nvbw -> R.string.provider_nvbw
        RtaChicago -> R.string.provider_rta_chicago
        Stv -> R.string.provider_stv
        Tlem -> R.string.provider_tlem
        Vbl -> R.string.provider_vbl
        Vgn -> R.string.provider_vgn
        Vmv -> R.string.provider_vmv
        Vrn -> R.string.provider_vrn
        Vrr -> R.string.provider_vrr
        Vvm -> R.string.provider_vvm
        Vvo -> R.string.provider_vvo
        Vvs -> R.string.provider_vvs
        Vvv -> R.string.provider_vvv
        Eireann -> R.string.provider_eireann
        Ns -> R.string.provider_ns
        Rt -> R.string.provider_rt
        Bayern -> R.string.provider_bayern
        Linz -> R.string.provider_linz
        Sydney -> R.string.provider_sydney
        Wien -> R.string.provider_wien
        Negentwee -> R.string.provider_negentwee
    }

    fun region(): ProviderRegion = when (this) {
        Bayern, Bsvag, Ding, Gvh, Kvv, Mvg, Mvv, Nvbw, Vgn, Vmv, Vrn, Vrr, Vvm, Vvo, Vvs, Vvv -> ProviderRegion.Germany
        Dub -> ProviderRegion.UnitedArabEmirates
        Linz, Stv, Wien -> ProviderRegion.Austria
        Mersey -> ProviderRegion.UnitedKingdom
        RtaChicago -> ProviderRegion.UnitedStates
        Sydney -> ProviderRegion.Australia
        Tlem -> ProviderRegion.UnitedKingdom
        Vbl -> ProviderRegion.Switzerland
        Eireann -> ProviderRegion.Ireland
        Ns, Negentwee -> ProviderRegion.Netherlands
        Rt -> ProviderRegion.Europe
    }

    fun url(): String = when (this) {
        Bayern -> "https://beg.bahnland-bayern.de"
        Bsvag -> "https://www.bsvg.net"
        Ding -> "https://www.ding.eu"
        Dub -> "https://www.rta.ae/wps/portal/rta/ae/public-transport"
        Gvh -> "https://www.uestra.de/fahrplan/auskunft"
        Kvv -> "https://www.kvv.de"
        Linz -> "https://www.linzag.at"
        Mersey -> "https://merseytravel.gov.uk"
        Mvg -> "https://www.mvg.de"
        Mvv -> "https://www.mvv-muenchen.de"
        Nvbw -> "https://www.nvbw.de"
        RtaChicago -> "https://www.rtachicago.org"
        Stv -> "https://www.verbundlinie.at"
        Sydney -> "https://transportnsw.info"
        Tlem -> "https://www.traveline.info"
        Vbl -> "https://www.vbl.ch"
        Vgn -> "https://www.vgn.de"
        Vmv -> "https://www.vmv-mbh.de"
        Vrn -> "https://www.vrn.de"
        Vrr -> "https://www.vrr.de"
        Vvm -> "https://www.vvm-info.de"
        Vvo -> "https://www.vvo-online.de"
        Vvs -> "https://www.vvs.de"
        Vvv -> "https://vogtlandauskunft.de"
        Wien -> "https://www.wienerlinien.at"
        Eireann -> "https://www.irishrail.ie"
        Ns -> "https://www.ns.nl"
        Rt -> "https://www.railteam.eu"
        Negentwee -> "https://www.9292.nl"
    }

    fun icon(): Uri? = when (this) {
        Bayern -> TODO()
        Bsvag -> TODO()
        Ding -> TODO()
        Dub -> TODO()
        Gvh -> TODO()
        Kvv -> TODO()
        Linz -> TODO()
        Mersey -> TODO()
        Mvg -> TODO()
        Mvv -> TODO()
        Nvbw -> TODO()
        RtaChicago -> TODO()
        Stv -> TODO()
        Sydney -> TODO()
        Tlem -> TODO()
        Vbl -> TODO()
        Vgn -> TODO()
        Vmv -> TODO()
        Vrn -> TODO()
        Vrr -> TODO()
        Vvm -> TODO()
        Vvo -> TODO()
        Vvs -> TODO()
        Vvv -> TODO()
        Wien -> TODO()
        Eireann -> TODO()
        Ns -> TODO()
        Rt -> TODO()
        Negentwee -> TODO()
    }?.let { Uri.parse(it) }
}