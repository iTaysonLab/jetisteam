package bruhcollective.itaysonlab.cobalt.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult

@Composable
internal fun ExceptionPage(
    result: CobaltScreenResult.Error,
    modifier: Modifier = Modifier
) {
    val board = LocalClipboardManager.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.err_text), style = MaterialTheme.typography.bodyLarge)

            TextButton(onClick = {
                board.setText(buildAnnotatedString {
                    append(result.exception.message.orEmpty())
                    appendLine()
                    append(result.exception.stackTraceToString())
                })
            }) {
                Text(stringResource(R.string.err_act_copy))
            }
        }
    }
}