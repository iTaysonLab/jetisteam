package bruhcollective.itaysonlab.microapp.home

import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.core.mapArgs
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments

abstract class HomeMicroapp : NestedMicroappEntry {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.StoreHome.url

    fun wishlistDestination(steamId: Long) = Routes.Wishlist.mapArgs(mapOf(
        CommonArguments.SteamId to steamId
    ))

    internal object Arguments {
        val GameId = navArgument("gameId") {
            type = NavType.IntType
        }
    }

    internal object Routes {
        const val NavGraph = "@home"

        val StoreHome = DestNode("store/home")
        val Wishlist = DestNode("store/{${CommonArguments.SteamId.name}}/wishlist")
    }
}