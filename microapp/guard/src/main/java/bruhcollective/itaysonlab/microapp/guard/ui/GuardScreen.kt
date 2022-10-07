package bruhcollective.itaysonlab.microapp.guard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateButton
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardScreen(
    viewModel: GuardViewModel = hiltViewModel(),
    dbg: () -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.guard))
            }
        )
    }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Rounded.Lock, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)

                Spacer(Modifier.height(8.dp))

                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineMedium) {
                    Text(text = stringResource(id = R.string.guard_title), modifier = Modifier.padding(horizontal = 16.dp))
                }

                Spacer(Modifier.height(4.dp))

                Text(text = stringResource(id = R.string.guard_desc), textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))

                Spacer(Modifier.height(8.dp))

                var inLoadingState by remember { mutableStateOf(false) }

                StateButton(onClick = dbg, inLoadingState = inLoadingState) {
                    Text(text = stringResource(id = R.string.guard_setup))
                }
            }
        }
    }
}