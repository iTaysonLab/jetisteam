package bruhcollective.itaysonlab.microapp.guard.ui.variants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.microapp.guard.R

@Composable
fun NoGuardScreen(
    modifier: Modifier,
    onAddClicked: () -> Unit,
    connectedToSteam: Boolean
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineMedium) {
                Text(
                    text = stringResource(id = R.string.guard_title),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(id = R.string.guard_desc),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = onAddClicked, enabled = connectedToSteam) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.guard_setup))
            }
        }
    }
}