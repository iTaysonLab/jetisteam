package bruhcollective.itaysonlab.jetisteam.guard.bottom_sheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRemoveSheetComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.ui.components.BottomSheetLayout
import bruhcollective.itaysonlab.jetisteam.ui.components.ResizableCircularIndicator
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardRemoveSheet(
    component: GuardRemoveSheetComponent,
) {
    val isRemoving by component.isRemovalInProgress.subscribeAsState()

    ModalBottomSheet(onDismissRequest = component::dismiss) {
        BottomSheetLayout(
            title = {
                stringResource(id = R.string.guard_remove_sheet_header)
            }, subtitle = {
                buildAnnotatedString {
                    append(stringResource(id = R.string.guard_for))
                    append(" ")
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(component.username)
                    }
                }
            }
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                ), modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.guard_remove_sheet_text),
                    modifier = Modifier.padding(16.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.guard_remove_sheet_warn),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
                        text = stringResource(id = R.string.guard_remove_sheet_action_cancel),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = component::confirmDeletion,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    if (isRemoving) {
                        ResizableCircularIndicator(
                            indicatorSize = 19.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.guard_remove_sheet_action_remove),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}