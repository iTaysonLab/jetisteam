package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.repository.TwoFactorRepository
import javax.inject.Inject

class RemoveSg @Inject constructor(
    private val tfaRepository: TwoFactorRepository
) {
    suspend operator fun invoke(rvCode: String, reason: Int, scheme: Int) = tfaRepository.remove(rvCode, reason, scheme, false)
}