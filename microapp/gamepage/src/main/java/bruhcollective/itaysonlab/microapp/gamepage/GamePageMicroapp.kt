package bruhcollective.itaysonlab.microapp.gamepage

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry

abstract class GamePageMicroapp : ComposableMicroappEntry {
    override val microappRoute = "game/${GAME_ID}"

    override val arguments: List<NamedNavArgument> =
        listOf(
            navArgument(GAME_ID) {
                type = NavType.StringType
                nullable = false
            }
        )

    fun gameDestination(gameId: String) = "${microappRoute}/$gameId"

    internal companion object {
        const val GAME_ID = "gameId"
    }
}