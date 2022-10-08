package bruhcollective.itaysonlab.jetisteam.usecases.twofactor

import bruhcollective.itaysonlab.jetisteam.controllers.UuidController
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.AuthRepository
import bruhcollective.itaysonlab.jetisteam.repository.TwoFactorRepository
import javax.inject.Inject

class InitializeAddTfa @Inject constructor(
    private val tfaRepository: TwoFactorRepository,
    private val uuidController: UuidController
) {
    suspend operator fun invoke(
        steamID: SteamID
    ) = tfaRepository.addAuthenticator(steamID = steamID, uuid = uuidController.uuid)
}