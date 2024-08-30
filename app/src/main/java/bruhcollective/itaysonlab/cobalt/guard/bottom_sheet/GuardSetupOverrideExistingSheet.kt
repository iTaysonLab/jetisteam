package bruhcollective.itaysonlab.cobalt.guard.bottom_sheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.setup.alert.GuardAlreadyExistsAlertComponent
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.ui.components.BottomSheetLayout
import bruhcollective.itaysonlab.cobalt.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.cobalt.ui.components.ResizableCircularIndicator
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardSetupOverrideExistingSheet(
    component: GuardAlreadyExistsAlertComponent
) {
    val isConfirming by component.isConfirmingReplacement.subscribeAsState()

    ModalBottomSheet(
        onDismissRequest = component::dismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        contentWindowInsets = { EmptyWindowInsets }
    ) {
        BottomSheetLayout(
            title = {
                stringResource(id = R.string.guard_setup_move)
            }
        ) {
            Text(
                text = stringResource(id = R.string.guard_setup_move_desc),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(Modifier.padding(horizontal = 16.dp)) {
                Button(
                    onClick = component::dismiss,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = stringResource(id = R.string.guard_setup_move_cancel),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = component::confirm,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    if (isConfirming) {
                        ResizableCircularIndicator(
                            indicatorSize = 19.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.guard_setup_move_allow),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}