package bruhcollective.itaysonlab.microapp.library.ui

import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.library
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LibraryScreenViewModel @Inject constructor(
    private val steamClient: HostSteamClient
) : ViewModel() {
    val allCollections get() = steamClient.client.library.userCollections
    fun collectionFlow(forId: String) = steamClient.client.library.getAppsInCollection(forId)
}