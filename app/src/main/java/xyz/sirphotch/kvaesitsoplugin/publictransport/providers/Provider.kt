package xyz.sirphotch.kvaesitsoplugin.publictransport.providers

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
    Europe

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

    fun localizedName(): String = when (this) {
        Bsvag -> "Braunschweig (BSVAG)"
        Ding -> "Donau-Iller (DING)"
        Dub -> "Dubai"
        Gvh -> "Hannover (GVH)"
        Kvv -> "Karlsruhe (KVV)"
        Mersey -> "Liverpool (Mersey)"
        Mvg -> "München (MVG)"
        Mvv -> "München (MVV)"
        Nvbw -> "Baden-Württemberg (NVBW)"
        RtaChicago -> "Chicago (RTA)"
        Stv -> "Steiermark (STV)"
        Tlem -> "Traveline south west (TLEM)"
        Vbl -> "Luzern (VBL)"
        Vgn -> "Mittelfranken (VGN)"
        Vmv -> "Mecklemburg-Vorpommern (VMV)"
        Vrn -> "Rhein-Neckar (VRN)"
        Vrr -> "Rhein-Ruhr (VRR)"
        Vvm -> "Mainfranken (VVM)"
        Vvo -> "Dresden (VVO)"
        Vvs -> "Stuttgart (VVS)"
        Vvv -> "Vogtland (VVV)"
        Eireann -> "Éireann"
        Ns -> "Nederlandse Spoorwegen"
        Rt -> "Railteam"
        else -> this.name
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
}