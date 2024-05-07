package bruhcollective.itaysonlab.jetisteam.guard.bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRecoveryCodeSheetComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.ui.components.BottomSheetLayout
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardRecoveryCodeSheet(
    component: GuardRecoveryCodeSheetComponent
) {
    ModalBottomSheet(
        onDismissRequest = component::dismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        contentWindowInsets = { EmptyWindowInsets }
    ) {
        BottomSheetLayout(
            title = {
                stringResource(id = R.string.guard_recovery)
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
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        4.dp
                    )
                ), modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = component.recoveryCode,
                        textAlign = TextAlign.Center,
                        modifier = Modifier,
                        fontSize = 40.sp,
                        letterSpacing = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = robotoMonoFontFamily,
                    )

                    Text(
                        text = stringResource(id = R.string.guard_recovery_hint),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(0.7f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.guard_recovery_desc),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FilledTonalButton(
                onClick = component::dismiss,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(16.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = stringResource(id = R.string.guard_remove_sheet_action_close),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}