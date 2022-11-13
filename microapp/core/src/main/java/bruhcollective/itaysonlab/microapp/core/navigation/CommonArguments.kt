package bruhcollective.itaysonlab.microapp.core.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object CommonArguments {
    val SteamId = navArgument("steamId") {
        type = NavType.LongType
    }

    val SteamIdWithDefault = navArgument("steamId") {
        type = NavType.LongType
        defaultValue = 0L
    }
}