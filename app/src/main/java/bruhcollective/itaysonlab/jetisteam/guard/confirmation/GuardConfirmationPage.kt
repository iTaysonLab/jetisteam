package bruhcollective.itaysonlab.jetisteam.guard.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.confirmation.GuardConfirmationComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.compose.accompanist.WebView
import bruhcollective.itaysonlab.jetisteam.compose.accompanist.rememberWebViewState
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.ui.components.StateButton
import bruhcollective.itaysonlab.jetisteam.ui.components.StateTonalButton
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardConfirmationPage(
    component: GuardConfirmationComponent
) {
    val isActionErrorOccurred by component.isActionErrorOccurred.subscribeAsState()
    val isConfirmationInProgress by component.isConfirmationInProgress.subscribeAsState()
    val isCancellationInProgress by component.isCancellationInProgress.subscribeAsState()

    val webViewUrl by component.detailsUrl.subscribeAsState()
    val webViewState = rememberWebViewState(url = webViewUrl)

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.guard_confirmation_detail))
            }, navigationIcon = {
                IconButton(onClick = component::onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null
                    )
                }
            })
        },
        bottomBar = {
            BottomBar(
                confirm = component.confirmButton,
                confirming = isConfirmationInProgress,
                onConfirm = component::onConfirmClicked,
                cancel = component.cancelButton,
                cancelling = isCancellationInProgress,
                onCancel = component::onCancelClicked,
                modifier = Modifier
                    .background(NavigationBarDefaults.containerColor)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        RoundedPage(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            WebView(
                state = webViewState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun BottomBar(
    confirm: String,
    onConfirm: () -> Unit,
    confirming: Boolean,
    cancel: String,
    onCancel: () -> Unit,
    cancelling: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        StateTonalButton(
            onClick = onCancel,
            inLoadingState = cancelling,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.large,
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = cancel)
        }

        StateButton(
            onClick = onConfirm,
            inLoadingState = confirming,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.large,
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = confirm)
        }
    }
}