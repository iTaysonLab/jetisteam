package bruhcollective.itaysonlab.microapp.guard.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.ui.components.CodeRow
import bruhcollective.itaysonlab.microapp.guard.ui.components.CodeRowState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardSetupScreen(
    onBackClicked: () -> Unit,
    state: CodeRowState,
    icon: () -> ImageVector,
    title: String,
    hint: String
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {}, navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = stringResource(id = android.R.string.cancel))
                }
            }
        )
    }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = icon(), contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)

                Spacer(Modifier.height(8.dp))

                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineMedium) {
                    Text(text = title, modifier = Modifier.padding(horizontal = 16.dp))
                }

                Spacer(Modifier.height(4.dp))

                Text(text = hint, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))

                Spacer(Modifier.height(16.dp))

                CodeRow(state = state)

                /*Spacer(Modifier.height(4.dp))

                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.guard_setup_enter_code_resend))
                }*/
            }
        }
    }
}