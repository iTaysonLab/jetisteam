package bruhcollective.itaysonlab.jetisteam.usecases.profile

import bruhcollective.itaysonlab.jetisteam.mappers.FriendProfile
import bruhcollective.itaysonlab.jetisteam.repository.FriendsRepository
import bruhcollective.itaysonlab.jetisteam.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProfileFriends @Inject constructor(
    private val friendsRepository: FriendsRepository,
    private val apiService: ApiService,
) {

    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        friendsRepository.getFriendsList().friendslist?.let {
            apiService.resolvePlayers(
                it.friends.filter { f -> f.efriendrelationship == 3 }
                    .map { f -> f.ulfriendid }.joinToString(separator = ",")
            ).players.map(::FriendProfile)
        } ?: listOf()
    }

}