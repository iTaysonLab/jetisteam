package bruhcollective.itaysonlab.microapp.gamepage

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry

abstract class GamePageMicroapp : ComposableMicroappEntry {
    override val microappRoute = "game/{$GAME_ID}"

    override val arguments: List<NamedNavArgument> =
        listOf(
            navArgument(GAME_ID) {
                type = NavType.IntType
                nullable = false
            }
        )

    companion object {
        internal const val GAME_ID = "gameId"

        fun gameDestination(gameId: Int) = "game/${gameId}"
    }
}