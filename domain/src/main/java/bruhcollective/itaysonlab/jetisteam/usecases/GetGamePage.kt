package bruhcollective.itaysonlab.jetisteam.usecases

import bruhcollective.itaysonlab.jetisteam.repository.GameRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetGamePage @Inject constructor(private val gamePageRepository: GameRepository) {
    suspend operator fun invoke(gameId: Int) = coroutineScope {
        val gamePage = async { gamePageRepository.getGame(gameId) }

        return@coroutineScope GamePage(
            gameName = gamePage.await().name,
            gameDescription = gamePage.await().fullDescription
        )
    }

    class GamePage(
        val gameName: String,
        val gameDescription: String
    )
}