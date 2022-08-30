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
            ids = listOf(StoreItemID.newBuilder().setAppid(gameId).build()),
            dataRequest = StoreBrowseItemDataRequest.newBuilder()
                .setIncludeTagCount(5)
                .setIncludeAssets(true)
                .setIncludePlatforms(true)
                .setIncludeBasicInfo(true)
                .setIncludeRatings(true)
                .setIncludeAllPurchaseOptions(true)
                .setIncludeReviews(true)
                .setIncludeTrailers(true)
                .setIncludeSupportedLanguages(true)
                .setIncludeScreenshots(true)
                .setIncludeRelease(true)
                .build()
        ).storeItemsList.first()

        val tags = storeRepository.getLocalizedTags(storeItem.tagidsList)
            .let { locale ->
                storeItem.tagsList.sortedByDescending { it.weight }
                    .map { locale[it.tagid]!! to it.tagid }
            }

        val combinedScreenshots =
            (storeItem.screenshots.allAgesScreenshotsList + storeItem.screenshots.matureContentScreenshotsList)
                .sortedBy { it.ordinal }
                .map { "${CdnUrlUtil.MEDIA_CDN_URL}/${it.filename}" }

        val langMatrix = storeItem.supportedLanguagesList.mapNotNull {
            LanguageMatrixEntry(
                language = Language.elanguageMap[it.elanguage] ?: return@mapNotNull null,
                ui = it.supported,
                fullAudio = it.fullAudio,
                subtitles = it.subtitles,
            )
        }

        return GamePage(
            details = details,
            tags = tags,
            screenshots = combinedScreenshots,
            languageMatrix = langMatrix,
            reviewsInfo = ReviewsInfo(
                storeItem.reviews.summaryFiltered.reviewCount,
                storeItem.reviews.summaryFiltered.percentPositive,
                storeItem.reviews.summaryFiltered.reviewScore,
                storeItem.reviews.summaryFiltered.reviewScoreLabel
            ),
            shareUrl = storeItem.storeUrlPath,
            platformInfo = PlatformInfo(
                windows = storeItem.platforms.windows,
                mac = storeItem.platforms.mac,
                linux = storeItem.platforms.linux,
                steamDeckSupport = SteamDeckSupport.Unsupported // TODO
            ),
            purchaseOptions = storeItem.purchaseOptionsList.map { PurchaseOption(it.purchaseOptionName, it.formattedFinalPrice) },
            releaseDate = storeItem.release.steamReleaseDate,
            backgroundUrl = CdnUrlUtil.buildAsset(storeItem.assets.assetUrlFormat, if (storeItem.assets.hasPageBackground()) storeItem.assets.pageBackground else storeItem.assets.libraryHero),
            logoUrl = CdnUrlUtil.buildAsset(storeItem.assets.assetUrlFormat, "logo.png")
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
        val backgroundUrl: String,
        val logoUrl: String
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