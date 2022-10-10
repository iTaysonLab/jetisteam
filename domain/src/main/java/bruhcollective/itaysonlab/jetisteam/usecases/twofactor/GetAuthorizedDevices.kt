package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.repository.AuthRepository
import bruhcollective.itaysonlab.jetisteam.repository.TwoFactorRepository
import javax.inject.Inject

class GetAuthorizedDevices @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        val devices = authRepository.enumerateTokens()
    }
}