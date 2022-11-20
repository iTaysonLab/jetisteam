package bruhcollective.itaysonlab.jetisteam.usecases.profile

import bruhcollective.itaysonlab.jetisteam.repository.ProfileRepository
import steam.player.ECommunityItemClass
import javax.inject.Inject

class SetProfileEquipment @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        itemId: Long,
        filter: ECommunityItemClass
    ) = profileRepository.setEquippedProfileItem(itemId, filter)
}