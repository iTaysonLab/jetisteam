package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.TwoFactorRepository
import javax.inject.Inject

class FinalizeAddTfa @Inject constructor(
    private val tfaRepository: TwoFactorRepository
) {
    suspend operator fun invoke(
        steamID: SteamID, smsCode: String, authCode: () -> Pair<String, Long>
    ): Boolean {
        val smsResponse = tfaRepository.finalizeWithSms(steamID = steamID, code = smsCode)

        return if (smsResponse.success == true) {
            if (smsResponse.want_more == true) {
                authCode().let { code ->
                    tfaRepository.finalizeWithCode(steamID = steamID, code = code.first, codeGenTime = code.second).success
                } ?: false
            } else {
                true
            }
        } else {
            false
        }
    }
}