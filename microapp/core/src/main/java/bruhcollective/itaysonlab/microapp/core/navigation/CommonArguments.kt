package bruhcollective.itaysonlab.microapp.core.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.NavigationResult

object CommonArguments {
    val SteamId = navArgument("steamId") {
        type = NavType.LongType
    }

    val SteamIdWithDefault = navArgument("steamId") {
        type = NavType.LongType
        defaultValue = 0L
    }

    val Result = navArgument("sys_result") {
        type = NavType.ParcelableType(NavigationResult::class.java)
    }
}