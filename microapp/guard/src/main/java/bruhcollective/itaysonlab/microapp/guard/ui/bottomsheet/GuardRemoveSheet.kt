package bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHandle
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHeader
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetSubtitle
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import bruhcollective.itaysonlab.microapp.guard.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardRemoveSheet(
    viewModel: GuardRemoveSheetViewModel = hiltViewModel(),
    onGuardRemoved: () -> Unit,
    onGuardRemovalCancelled: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()) {

        BottomSheetHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        BottomSheetHeader(
            text = stringResource(id = R.string.guard_confirm_sheet_header),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        BottomSheetSubtitle(text = buildAnnotatedString {
            append(stringResource(id = R.string.guard_as))
            append(" ")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                // append(viewModel.guardInstance.username)
            }
        }, modifier = Modifier.padding(bottom = 16.dp))


    }
}

@HiltViewModel
internal class GuardRemoveSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val steamId = SteamID(savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)!!.toLong())
}