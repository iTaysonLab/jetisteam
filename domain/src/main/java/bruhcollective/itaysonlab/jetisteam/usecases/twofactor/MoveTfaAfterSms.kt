package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.repository.TwoFactorRepository
import javax.inject.Inject

class MoveTfaAfterSms @Inject constructor(
    private val tfaRepository: TwoFactorRepository
) {
    suspend operator fun invoke(code: String) = tfaRepository.moveFinish(code)
}