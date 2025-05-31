package bruhcollective.itaysonlab.cobalt.library.screenshots

import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue

internal class DefaultScreenshotsFilterSheetComponent (
    private val currentQuery: ScreenshotsComponent.FileQuery,
    private val onQueryChanged: (ScreenshotsComponent.FileQuery) -> Unit,
    componentContext: ComponentContext
): ScreenshotsFilterSheetComponent, ComponentContext by componentContext {
    override val sortOrder = MutableValue(currentQuery.sortOrder)

    override fun setSortOrder(value: PublishedFiles.PersonalSortOrder) {
        sortOrder.value = value
    }

    override val privacyFilter = MutableValue(currentQuery.privacyFilter)

    override fun setPrivacyFilter(value: PublishedFiles.PersonalPrivacyFilter) {
        privacyFilter.value = value
    }

    override val browseFilter = MutableValue(currentQuery.browseFilter)

    override fun setBrowseFilter(value: PublishedFiles.PersonalBrowseFilter) {
        browseFilter.value = value
    }

    override fun applyChanges() {
        onQueryChanged(currentQuery.copy(
            sortOrder = sortOrder.value,
            privacyFilter = privacyFilter.value,
            browseFilter = browseFilter.value,
        ))
    }
}