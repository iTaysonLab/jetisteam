package bruhcollective.itaysonlab.jetisteam.usecases.profile

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileEquipment
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileTheme
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.ProfileRepository
import javax.inject.Inject

class GetProfileEquips @Inject constructor(
    private val steamSessionController: SteamSessionController,
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        steamId: SteamID = steamSessionController.steamId()
    ): ProfileEquips {
        return ProfileEquips(
            equipment = ProfileEquipment(profileRepository.getProfileItems(steamId.steamId)),
            currentTheme = ProfileTheme(profileRepository.getProfileCustomization(steamId.steamId).profile_theme!!)
        )
    }

    class ProfileEquips(
        val equipment: ProfileEquipment,
        val currentTheme: ProfileTheme?,
    )
}