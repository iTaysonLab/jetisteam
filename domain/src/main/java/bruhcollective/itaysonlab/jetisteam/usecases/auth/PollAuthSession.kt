package bruhcollective.itaysonlab.jetisteam.usecases.auth

import bruhcollective.itaysonlab.jetisteam.repository.AuthRepository
import javax.inject.Inject

class PollAuthSession @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean = authRepository.pollStatus()
}