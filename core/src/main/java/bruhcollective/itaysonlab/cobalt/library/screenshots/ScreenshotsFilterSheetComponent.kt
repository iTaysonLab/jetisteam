package bruhcollective.itaysonlab.cobalt.library.screenshots

import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalBrowseFilter
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalPrivacyFilter
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalSortOrder
import com.arkivanov.decompose.value.Value

interface ScreenshotsFilterSheetComponent {
    val sortOrder: Value<PersonalSortOrder>
    fun setSortOrder(value: PersonalSortOrder)

    val privacyFilter: Value<PersonalPrivacyFilter>
    fun setPrivacyFilter(value: PersonalPrivacyFilter)

    val browseFilter: Value<PersonalBrowseFilter>
    fun setBrowseFilter(value: PersonalBrowseFilter)

    fun applyChanges()
}