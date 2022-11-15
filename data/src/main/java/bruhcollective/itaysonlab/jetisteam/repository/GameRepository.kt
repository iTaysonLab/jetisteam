package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.controllers.CacheService
import bruhcollective.itaysonlab.jetisteam.controllers.LocaleService
import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.models.*
import bruhcollective.itaysonlab.jetisteam.service.GameService
import com.squareup.moshi.Moshi
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class GameRepository @Inject constructor(
    private val gameService: GameService,
    private val cacheService: CacheService,
    private val localeService: LocaleService,
    private val steamSessionController: SteamSessionController,
    private val moshi: Moshi
) {
    companion object {
        private val GameReviewPreviewCacheDuration = 1.hours
        private val GameCompatCacheDuration = 2.hours
        private val GameDetailCacheDuration = 24.hours
        private val GameDeckReportCacheDuration = 7.days
    }

    suspend fun getGameDetails(appId: String, force: Boolean = false): GameFullDetailsData {
        return cacheService.jsonEntry<GameFullDetailsData>(
            key = key(appId, "details"),
            force = force,
            maxCacheTime = GameDetailCacheDuration,
            adapter = moshi.adapter(GameFullDetailsData::class.java),
            networkFunc = {
                gameService.getGameDetails(appId = appId, language = localeService.language, country = localeService.myCountry()).values.first().data!!
            }, defaultFunc = {
                error("This should not be seen!")
            }
        )
    }

    suspend fun getDeckCompat(appId: String, force: Boolean = false): SteamDeckSupportReport {
        return cacheService.jsonEntry<SteamDeckSupportReport>(
            key = key(appId, "deckReport"),
            force = force,
            maxCacheTime = GameDeckReportCacheDuration,
            adapter = moshi.adapter(SteamDeckSupportReport::class.java),
            networkFunc = {
                gameService.getDeckReport(appId = appId, language = localeService.language, country = localeService.myCountry()).results
            }, defaultFunc = {
                SteamDeckSupportReport(appId = appId.toInt(), resolvedCategory = 0, resolvedItems = emptyList())
            }
        )
    }

    suspend fun getReviewsPreview(appId: String, force: Boolean = false): Reviews {
        return cacheService.jsonEntry<Reviews>(
            key = key(appId, "reviewsPreview"),
            force = force,
            maxCacheTime = GameReviewPreviewCacheDuration,
            adapter = moshi.adapter(Reviews::class.java),
            networkFunc = {
                gameService.getReviews(appId = appId, language = localeService.language, reviewLanguages = localeService.language)
            }, defaultFunc = {
                Reviews(success = 0, summary = Reviews.ReviewQuerySummary(0, 0, "", 0, 0, 0), reviews = emptyList())
            }
        )
    }

    suspend fun getGameCompat(appId: String, force: Boolean = false): GameCompatDetails {
        return cacheService.jsonEntry<GameCompatDetails>(
            key = key(appId, "gameCompat"),
            force = force,
            maxCacheTime = GameCompatCacheDuration,
            adapter = moshi.adapter(GameCompatDetails::class.java),
            networkFunc = {
                gameService.getGameCompat(appIds = appId).values.first()
            }, defaultFunc = {
                GameCompatDetails(success = false, data = null)
            }
        )
    }

    private fun key(appId: String, type: String) = "steam.apps.$appId.$type"
}