package bruhcollective.itaysonlab.microapp.steam_wrapped

import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.core.mapArgs
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments

abstract class SteamWrappedMicroapp: NestedMicroappEntry {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.Main.url

    fun entryDestination(steamId: Long) = Routes.Main.mapArgs(mapOf(
        CommonArguments.SteamId to steamId
    ))

    object Routes {
        const val NavGraph = "@specials/2022"
        val Main = DestNode("special/wrapped2022/{${CommonArguments.SteamId.name}}")
    }
}