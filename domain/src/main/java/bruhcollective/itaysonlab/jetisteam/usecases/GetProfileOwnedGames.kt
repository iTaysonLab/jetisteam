package bruhcollective.itaysonlab.jetisteam.usecases

import bruhcollective.itaysonlab.jetisteam.mappers.OwnedGame
import bruhcollective.itaysonlab.jetisteam.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProfileOwnedGames @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(steamId: Long) = withContext(Dispatchers.Default) { profileRepository.getOwnedGames(steamId).gamesList.map(::OwnedGame) }
}