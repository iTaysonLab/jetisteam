package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.repository.AuthRepository
import okio.ByteString
import steam.auth.ESessionPersistence
import javax.inject.Inject

class UpdateSessionWithMobileAuth @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        version: Int,
        clientId: Long,
        steamId: Long,
        allow: Boolean,
        signature: ByteString,
        persistence: ESessionPersistence
    ) {
        authRepository.updateSessionWithMobileAuth(
            version = version,
            clientId = clientId,
            steamId = steamId,
            signature = signature,
            confirm = allow,
            persistence = persistence
        )
    }
}