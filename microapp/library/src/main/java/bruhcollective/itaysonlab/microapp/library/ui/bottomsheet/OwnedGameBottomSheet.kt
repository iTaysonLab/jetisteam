package bruhcollective.itaysonlab.microapp.library.ui.bottomsheet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetLayout

@Composable
fun OwnedGameBottomSheet(
    viewModel: OwnedGameBottomSheetViewModel = hiltViewModel()
) {
    BottomSheetLayout {
        Text(text = "TODO")
    }
}