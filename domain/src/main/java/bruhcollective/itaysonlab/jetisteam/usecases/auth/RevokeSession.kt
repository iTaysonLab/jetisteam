package bruhcollective.itaysonlab.jetisteam.usecases.auth

import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.AuthRepository
import okio.ByteString
import javax.inject.Inject

class RevokeSession @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        steamId: SteamID, tokenId: Long, signature: ByteString
    ) {
        authRepository.revokeSession(steamId, tokenId, signature)
    }
}