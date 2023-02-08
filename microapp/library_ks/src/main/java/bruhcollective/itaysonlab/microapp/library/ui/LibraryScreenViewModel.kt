package bruhcollective.itaysonlab.microapp.library.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.microapp.library.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LibraryScreenViewModel @Inject constructor(
    savedState: SavedStateHandle
) : ViewModel() {
    var searchQuery by mutableStateOf(TextFieldValue(""))
    var sortMode by mutableStateOf(SortMode.ByName)

    enum class SortMode(@StringRes val nameRes: Int, val iconFunc: () -> ImageVector) {
        ByName(
            R.string.library_sort_name,
            { androidx.compose.material.icons.Icons.Rounded.SortByAlpha }),
        ByPlaytime(
            R.string.library_sort_time,
            { androidx.compose.material.icons.Icons.Rounded.Timer }),
        ByLaunchDate(
            R.string.library_sort_date,
            { androidx.compose.material.icons.Icons.Rounded.History }),
    }

    private inline fun <T> List<T>.filterIf(
        predicate: Boolean,
        crossinline func: (T) -> Boolean
    ): List<T> {
        return if (predicate) {
            this.filter(func)
        } else {
            this
        }
    }
}