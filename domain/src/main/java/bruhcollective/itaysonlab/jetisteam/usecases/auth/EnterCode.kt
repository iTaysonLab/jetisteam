package bruhcollective.itaysonlab.jetisteam.usecases.auth

import bruhcollective.itaysonlab.jetisteam.repository.AuthRepository
import javax.inject.Inject

class EnterCode @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(code: String): Boolean = authRepository.enterDeviceCode(code)
}