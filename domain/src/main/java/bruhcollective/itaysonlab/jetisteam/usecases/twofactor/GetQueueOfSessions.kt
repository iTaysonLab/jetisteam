package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.repository.AuthRepository
import javax.inject.Inject

class GetQueueOfSessions @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Long {
        return try {
            authRepository.getQueueOfSessions().client_ids.lastOrNull() ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}