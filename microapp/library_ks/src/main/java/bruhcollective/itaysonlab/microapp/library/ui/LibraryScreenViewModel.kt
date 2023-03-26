package bruhcollective.itaysonlab.microapp.library.ui

import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.ksteam.handlers.Pics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LibraryScreenViewModel @Inject constructor(
    private val steamPics: Pics
) : ViewModel() {
    val picsState get() = steamPics.isPicsAvailable
}