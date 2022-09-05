package bruhcollective.itaysonlab.jetisteam.usecases

import bruhcollective.itaysonlab.jetisteam.models.GameDetails
import bruhcollective.itaysonlab.jetisteam.models.Language
import bruhcollective.itaysonlab.jetisteam.models.SteamDeckSupport
import bruhcollective.itaysonlab.jetisteam.repository.GameRepository
import bruhcollective.itaysonlab.jetisteam.repository.StoreRepository
import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import steam.common.StoreBrowseItemDataRequest
import steam.common.StoreItemID
import javax.inject.Inject

class GetGamePage @Inject constructor(
    private val gameRepository: GameRepository,
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(gameId: Int): GamePage {
        val details = gameRepository.getGameDetails(gameId.toString())

        val storeItem = storeRepository.getItems(
            ids = listOf(StoreItemID(appid = gameId)),
            dataRequest = StoreBrowseItemDataRequest(
                include_tag_count = 5,
                include_assets = true,
                include_platforms = true,
                include_basic_info = true,
                include_ratings = true,
                include_all_purchase_options = true,
                include_reviews = true,
                include_trailers = true,
                include_supported_languages = true,
                include_screenshots = true,
                include_release = true,
            )
        ).store_items.first()

        val tags = storeRepository.getLocalizedTags(storeItem.tagids)
            .let { locale ->
                storeItem.tags.sortedByDescending { it.weight }
                    .map { locale[it.tagid]!! to it.tagid!! }
            }

        val combinedScreenshots =
            (storeItem.screenshots!!.all_ages_screenshots + storeItem.screenshots!!.mature_content_screenshots)
                .sortedBy { it.ordinal }
                .map { "${CdnUrlUtil.MEDIA_CDN_URL}/${it.filename}" }

        val langMatrix = storeItem.supported_languages.mapNotNull {
            LanguageMatrixEntry(
                language = Language.elanguageMap[it.elanguage] ?: return@mapNotNull null,
                ui = it.supported ?: false,
                fullAudio = it.full_audio ?: false,
                subtitles = it.subtitles ?: false,
            )
        }

        return GamePage(
            details = details,
            tags = tags,
            screenshots = combinedScreenshots,
            languageMatrix = langMatrix,
            reviewsInfo = ReviewsInfo(
                storeItem.reviews?.summary_filtered?.review_count ?: 0,
                storeItem.reviews?.summary_filtered?.percent_positive ?: 0,
                storeItem.reviews?.summary_filtered?.review_score ?: 0,
                storeItem.reviews?.summary_filtered?.review_score_label.orEmpty()
            ),
            shareUrl = storeItem.store_url_path.orEmpty(),
            platformInfo = PlatformInfo(
                windows = storeItem.platforms?.windows ?: false,
                mac = storeItem.platforms?.mac ?: false,
                linux = storeItem.platforms?.linux ?: false,
                steamDeckSupport = SteamDeckSupport.Unsupported // TODO
            ),
            purchaseOptions = storeItem.purchase_options.map { PurchaseOption(it.purchase_option_name.orEmpty(), it.formatted_final_price.orEmpty()) },
            releaseDate = storeItem.release?.steam_release_date ?: 0,
            backgroundUrl = CdnUrlUtil.buildAsset(storeItem.assets?.asset_url_format, if (storeItem.assets?.page_background != null) storeItem.assets?.page_background else storeItem.assets?.library_hero),
            logoUrl = CdnUrlUtil.buildAsset(storeItem.assets?.asset_url_format, "logo.png")
        )
    }

    class GamePage(
        val details: GameDetails,
        val tags: List<Pair<String, Int>>,
        val screenshots: List<String>,
        val languageMatrix: List<LanguageMatrixEntry>,
        val reviewsInfo: ReviewsInfo,
        val shareUrl: String,
        val platformInfo: PlatformInfo,
        val purchaseOptions: List<PurchaseOption>,
        val releaseDate: Int,
        val backgroundUrl: String?,
        val logoUrl: String?
    )

    class LanguageMatrixEntry(
        val language: Language,
        val ui: Boolean,
        val fullAudio: Boolean,
        val subtitles: Boolean,
    )

    class ReviewsInfo(
        val reviews: Int,
        val positivePercent: Int,
        val score: Int,
        val scoreLabel: String
    )

    class PlatformInfo(
        val windows: Boolean,
        val mac: Boolean,
        val linux: Boolean,
        val steamDeckSupport: SteamDeckSupport
    )

    class PurchaseOption(
        val name: String,
        val price: String
    )
}