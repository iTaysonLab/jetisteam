package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.repository.TwoFactorRepository
import javax.inject.Inject

class MoveTfaRequestSms @Inject constructor(
    private val tfaRepository: TwoFactorRepository
) {
    suspend operator fun invoke() = tfaRepository.moveStart()
}