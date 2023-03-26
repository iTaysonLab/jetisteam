package bruhcollective.itaysonlab.microapp.library.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.ksteam.handlers.Pics
import bruhcollective.itaysonlab.microapp.library.ui.library_pages.LibraryMainContainer

@Composable
internal fun LibraryScreen(
    onApplicationClick: (Int) -> Unit,
    // onRemoteClick: (Long, List<CClientComm_GetAllClientLogonInfo_Response_Session>) -> Unit,
    viewModel: LibraryScreenViewModel = hiltViewModel()
) {
    val picsState by viewModel.picsState.collectAsStateWithLifecycle()

    when (picsState) {
        Pics.PicsState.Ready -> {
            LibraryMainContainer(onApplicationClick)
        }

        Pics.PicsState.Initialization -> {
            FullscreenLoading()
        }

        else -> {
            LibraryLoadingPage(picsState)
        }
    }
}