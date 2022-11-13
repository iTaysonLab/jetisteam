package bruhcollective.itaysonlab.microapp.gamepage

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.mapArgs

abstract class GamePageMicroapp : ComposableMicroappEntry {
    override val microappRoute = InternalRoutes.Game.url
    override val arguments: List<NamedNavArgument> = listOf(Arguments.GameId)

    fun gameDestination(gameId: Int) = InternalRoutes.Game.mapArgs(mapOf(
        Arguments.GameId to gameId
    ))

    internal object Arguments {
        val GameId = navArgument("gameId") {
            type = NavType.IntType
        }
    }

    internal object InternalRoutes {
        val Game = DestNode("game/{${Arguments.GameId.name}}")
    }
}