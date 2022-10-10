package bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHandle
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import bruhcollective.itaysonlab.microapp.guard.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardMoreOptionsSheet(
    viewModel: GuardMoreOptionsViewModel = hiltViewModel(),
    onDevicesClicked: (Long) -> Unit,
    onRemoveClicked: (Long) -> Unit,
    onRecoveryClicked: (Long) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()) {

        BottomSheetHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        LazyColumn {
            item {
                ListItem(
                    leadingContent = {
                        Icon(imageVector = Icons.Rounded.Devices, contentDescription = null)
                    }, headlineText = {
                        Text(text = stringResource(id = R.string.guard_actions_devices))
                    }, modifier = Modifier.clickable(onClick = { onDevicesClicked(viewModel.steamId.steamId) }), colors = ListItemDefaults.colors(
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            item {
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            }

            item {
                ListItem(
                    leadingContent = {
                        Icon(imageVector = Icons.Rounded.Key, contentDescription = null)
                    }, headlineText = {
                        Text(text = stringResource(id = R.string.guard_recovery))
                    }, modifier = Modifier.clickable(onClick = { onRecoveryClicked(viewModel.steamId.steamId) }), colors = ListItemDefaults.colors(
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            item {
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            }

            item {
                ListItem(
                    leadingContent = {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                    }, headlineText = {
                        Text(text = stringResource(id = R.string.guard_actions_remove))
                    }, modifier = Modifier.clickable(onClick = { onRemoveClicked(viewModel.steamId.steamId) }), colors = ListItemDefaults.colors(
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@HiltViewModel
internal class GuardMoreOptionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val steamId = SteamID(savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)!!.toLong())
}