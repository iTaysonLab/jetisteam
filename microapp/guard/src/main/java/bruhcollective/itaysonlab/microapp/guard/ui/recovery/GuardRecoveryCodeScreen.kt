package bruhcollective.itaysonlab.microapp.guard.ui.recovery

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardRecoveryCodeScreen(
    viewModel: GuardRecoveryCodeViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), contentAlignment = Alignment.Center) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Rounded.SettingsSuggest, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)

                Spacer(Modifier.height(8.dp))

                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineMedium) {
                    Text(text = stringResource(id = R.string.guard_recovery), modifier = Modifier.padding(horizontal = 16.dp))
                }

                Spacer(Modifier.height(4.dp))

                Text(text = viewModel.revocationCode, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp), fontSize = 40.sp, letterSpacing = 12.sp, color = MaterialTheme.colorScheme.primary)

                Spacer(Modifier.height(4.dp))

                Text(text = stringResource(id = R.string.guard_recovery_hint), textAlign = TextAlign.Center, modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .alpha(0.7f))

                Spacer(Modifier.height(4.dp))

                Text(text = stringResource(id = R.string.guard_recovery_desc), textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))

                Spacer(Modifier.height(8.dp))

                Button(onClick = onBackClicked) {
                    Text(text = stringResource(id = R.string.guard_recovery_action))
                }
            }
        }
    }
}