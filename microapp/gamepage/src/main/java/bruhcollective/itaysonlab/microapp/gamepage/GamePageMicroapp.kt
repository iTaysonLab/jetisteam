package bruhcollective.itaysonlab.microapp.gamepage

import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.core.mapArgs

abstract class GamePageMicroapp : NestedMicroappEntry {
    override val graphRoute = InternalRoutes.NavGraph
    override val startDestination = "TODO"

    fun gameDestination(gameId: Int) = InternalRoutes.Game.mapArgs(mapOf(
        Arguments.GameId to gameId
    ))

    internal object Arguments {
        val GameId = navArgument("gameId") {
            type = NavType.IntType
        }
    }

    internal object InternalRoutes {
        const val NavGraph = "@gamepage"

        val Game = DestNode("game/{${Arguments.GameId.name}}")
        val DeckReport = DestNode("game/{${Arguments.GameId.name}}/deckReport")
    }
}