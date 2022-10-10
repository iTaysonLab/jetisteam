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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHandle
import bruhcollective.itaysonlab.microapp.guard.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardMoreOptionsSheet(

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
                    }, modifier = Modifier.clickable {

                    }, colors = ListItemDefaults.colors(
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
                    }, modifier = Modifier.clickable {

                    }, colors = ListItemDefaults.colors(
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
private fun ActionItem() {

}