package bruhcollective.itaysonlab.jetisteam.usecases.profile

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileEquipment
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileItem
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileTheme
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.ProfileRepository
import bruhcollective.itaysonlab.jetisteam.repository.StoreRepository
import steam.common.StoreBrowseItemDataRequest
import steam.common.StoreItem
import steam.common.StoreItemID
import steam.player.ECommunityItemClass
import javax.inject.Inject

class GetProfileEquipsAndOwned @Inject constructor(
    private val steamSessionController: SteamSessionController,
    private val profileRepository: ProfileRepository,
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        steamId: SteamID = steamSessionController.steamId(),
        filter: ECommunityItemClass
    ): ProfileEquips {
        val selected = profileRepository.getEquippedProfileItem(steamId.steamId, filter).let {
            ProfileItem(
                it ?: return@let null
            )
        }

        val owned = profileRepository.getProfileItemsOwned(filter).let {
            it.avatar_frames + it.animated_avatars + it.profile_modifiers + it.profile_backgrounds + it.mini_profile_backgrounds
        }.map(::ProfileItem)

        return ProfileEquips(
            steamID = steamId,
            owned = owned,
            current = selected,
            apps = storeRepository.getItems(
                ids = owned.map { StoreItemID(appid = it.appId) }.distinct(),
                dataRequest = StoreBrowseItemDataRequest(
                    include_assets = true
                )
            ).store_items.filter { it.appid != null }.associateBy { it.appid ?: 0 },
        )
    }

    class ProfileEquips(
        val steamID: SteamID,
        val apps: Map<Int, StoreItem>,
        val owned: List<ProfileItem>,
        val current: ProfileItem?
    )
}