package bruhcollective.itaysonlab.jetisteam.uikit.page

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import bruhcollective.itaysonlab.jetisteam.uikit.R

@Composable
fun FullscreenLoading() {
    Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier
            .align(Alignment.Center)
            .size(56.dp))
    }
}

@Composable
fun FullscreenError(
    onReload: () -> Unit,
    exception: Throwable
) {
    val ctx = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .align(Alignment.Center)
        ) {
            Icon(
                Icons.Rounded.Error, contentDescription = null, modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(56.dp)
                    .padding(bottom = 12.dp)
            )
            Text(
                stringResource(id = R.string.err_text),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    ctx.copy("Message: ${exception.message}\n\n" + exception.stackTraceToString())
                }) {
                Text(stringResource(id = R.string.err_act_copy))
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = { onReload() }) {
                Text(stringResource(id = R.string.err_act_reload))
            }
        }
    }
}

private fun Context.copy(txt: String) {
    getSystemService<ClipboardManager>()?.setPrimaryClip(ClipData.newPlainText("Jetisteam", txt))
}