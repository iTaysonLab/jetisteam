package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.repository.AuthRepository
import bruhcollective.itaysonlab.jetisteam.repository.TwoFactorRepository
import okio.Buffer
import steam.auth.CAuthentication_GetAuthSessionInfo_Response
import steam.auth.CAuthentication_RefreshToken_Enumerate_Response
import javax.inject.Inject

class GetQueueOfSessions @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Long {
        return authRepository.getQueueOfSessions().client_ids.firstOrNull() ?: 0L
    }
}