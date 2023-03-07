package bruhcollective.itaysonlab.microapp.library.ui.game_page.activity

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.ksteam.handlers.news
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
internal class GamepageActivityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hostSteamClient: HostSteamClient
): PageViewModel<List<GamepageActivityViewModel.ActivityItem>>() {
    private val dateFormat = DateTimeFormatter.ofPattern("dd MMM")
    private val appId = AppId(savedStateHandle.get<Int>(LibraryMicroapp.Arguments.ApplicationId.name) ?: 0)

    init {
        reload()
    }

    override suspend fun load() = withContext(Dispatchers.IO) {
        val listOfItems = mutableListOf<ActivityItem>()

        hostSteamClient.client.news.getUserNews(appId).groupBy {
            Instant.fromEpochSeconds(it.date.toLong())
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        }.asSequence().sortedByDescending {
            it.key
        }.forEach { (date, items) ->
            listOfItems.add(
                ActivityItem.DateSeparator(date.toJavaLocalDate().format(dateFormat))
            )

            listOfItems.addAll(items.map {
                ActivityItem.EntryContainer(it)
            })
        }

        return@withContext listOfItems
    }

    sealed class ActivityItem {
        class DateSeparator(
            val parsedDate: String
        ): ActivityItem()

        class EntryContainer(
            val item: ActivityFeedEntry
        ): ActivityItem()
    }
}