package bruhcollective.itaysonlab.microapp.library.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHandle
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHeader
import bruhcollective.itaysonlab.microapp.library.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PickRemoteDeviceBottomSheet(
    viewModel: PickRemoteDeviceViewModel = hiltViewModel(),
    onMachinePicked: (Long, Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 16.dp)
            .navigationBarsPadding()
    ) {
        BottomSheetHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        BottomSheetHeader(
            text = stringResource(id = R.string.library_remote_pick),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
        ) {
            viewModel.machines.forEachIndexed { index, machine ->
                ListItem(
                    headlineText = {
                        Text(text = machine.machine_name.orEmpty())
                    }, supportingText = {
                        Text(text = machine.os_name.orEmpty())
                    }, modifier = Modifier.clickable(onClick = {
                        onMachinePicked(viewModel.steamId.steamId, machine.client_instanceid ?: return@clickable)
                    }), colors = ListItemDefaults.colors(
                        leadingIconColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Transparent,
                    ), trailingContent = {
                        Icon(Icons.Rounded.ChevronRight, contentDescription = null)
                    }
                )

                if (index != viewModel.machines.lastIndex) {
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }
    }
}