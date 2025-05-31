package bruhcollective.itaysonlab.cobalt.library.screenshots

import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface SelectApplicationSheetComponent {
    val isLoading: Value<Boolean>
    val applications: Value<ImmutableList<ScreenshotsComponent.PickedApplication>>

    fun onClearFilterClicked()
    fun onApplicationClicked(app: ScreenshotsComponent.PickedApplication)
}