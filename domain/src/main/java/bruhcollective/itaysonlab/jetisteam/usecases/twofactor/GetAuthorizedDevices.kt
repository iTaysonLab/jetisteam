package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.repository.AuthRepository
import bruhcollective.itaysonlab.jetisteam.repository.TwoFactorRepository
import okio.Buffer
import steam.auth.CAuthentication_RefreshToken_Enumerate_Response
import javax.inject.Inject

class GetAuthorizedDevices @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): List<CAuthentication_RefreshToken_Enumerate_Response.RefreshTokenDescription> {
        return try {
            authRepository.enumerateTokens().refresh_tokens
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}