package bruhcollective.itaysonlab.jetisteam.usecases.specials

import bruhcollective.itaysonlab.jetisteam.controllers.CdnController
import bruhcollective.itaysonlab.jetisteam.controllers.UserService
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileCustomization
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileEquipment
import bruhcollective.itaysonlab.jetisteam.models.Player
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.GameRepository
import bruhcollective.itaysonlab.jetisteam.repository.ProfileRepository
import bruhcollective.itaysonlab.jetisteam.repository.SaleFeatureRepository
import bruhcollective.itaysonlab.jetisteam.repository.StoreRepository
import steam.common.StoreItem
import steam.salefeature.CUserYearInReviewStats
import javax.inject.Inject

class GetSteamReplay @Inject constructor(
    private val gameRepository: GameRepository,
    private val storeRepository: StoreRepository,
    private val cdnController: CdnController,
    private val userService: UserService,
    private val saleFeatureRepository: SaleFeatureRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(steamId: SteamID): SteamReplay {
        val yir = saleFeatureRepository.getYearInReview(steamId).stats!!

        /*val items = storeRepository.getItems(
            ids = listOf(StoreItemID(appid = appId)),
            dataRequest = StoreBrowseItemDataRequest(
                include_tag_count = 0,
                include_assets = true,
                include_platforms = false,
                include_basic_info = false,
                include_ratings = false,
                include_all_purchase_options = false,
                include_reviews = false,
                include_trailers = false,
                include_supported_languages = true,
                include_screenshots = false,
                include_release = false,
            )
        ).store_items.first()*/

        return SteamReplay(
            yearInReview = yir,
            storeItems = emptyMap(),
            profile = userService.resolveUsers(listOf(steamId.steamId)).values.first(),
            profileEquipment = ProfileEquipment(profileRepository.getProfileItems(steamId.steamId)),
            customization = ProfileCustomization(profileRepository.getProfileCustomization(steamId.steamId)),
        )
    }

    class SteamReplay(
        val yearInReview: CUserYearInReviewStats,
        val storeItems: Map<Int, StoreItem>,
        val profile: Player,
        val profileEquipment: ProfileEquipment,
        val customization: ProfileCustomization,
    )
}