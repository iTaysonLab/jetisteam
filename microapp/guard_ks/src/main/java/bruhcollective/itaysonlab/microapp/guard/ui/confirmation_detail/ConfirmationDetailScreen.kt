package bruhcollective.itaysonlab.microapp.guard.ui.confirmation_detail

import android.webkit.CookieManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateButton
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateTonalButton
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConfirmationDetailScreen(
    viewModel: ConfirmationDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onFinish: (ConfirmationDetailViewModel.ConfirmationDetailResult) -> Unit
) {
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val webViewState = rememberWebViewState(url = viewModel.webViewUrl)

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = stringResource(id = R.string.guard_confirmation_detail))
            }, navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            }, scrollBehavior = tas, colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
            ))
        }, contentWindowInsets = EmptyWindowInsets, modifier = Modifier
            .fillMaxSize()
            .nestedScroll(tas.nestedScrollConnection), bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                StateTonalButton(
                    onClick = { viewModel.submitAction(confirm = false, onFinish) },
                    shape = MaterialTheme.shapes.medium,
                    inLoadingState = viewModel.operationStatus == ConfirmationDetailViewModel.Operation.Revoking,
                    modifier = Modifier.weight(1f).heightIn(min = 56.dp)
                ) {
                    Text(viewModel.sessionData.cancelButtonText)
                }

                Spacer(modifier = Modifier.width(8.dp))

                StateButton(
                    onClick = { viewModel.submitAction(confirm = true, onFinish) },
                    shape = MaterialTheme.shapes.medium,
                    inLoadingState = viewModel.operationStatus == ConfirmationDetailViewModel.Operation.Confirming,
                    modifier = Modifier.weight(1f).heightIn(min = 56.dp)
                ) {
                    Text(viewModel.sessionData.acceptButtonText)
                }
            }
        }
    ) { innerPadding ->
        RoundedPage(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            WebView(state = webViewState, modifier = Modifier.fillMaxSize(), onCreated = {
                CookieManager.getInstance().setCookie("https://steamcommunity.com", "mobileClient=android; Path=/; Secure; HttpOnly")
                CookieManager.getInstance().setCookie("https://steamcommunity.com", "mobileClientVersion=777777 3.0.0; Path=/; Secure; HttpOnly")
                CookieManager.getInstance().setCookie("https://steamcommunity.com", "steamLoginSecure=${viewModel.buildSteamLoginSecureCookie()}; Path=/; Secure; HttpOnly")
                CookieManager.getInstance().flush()
            })
        }
    }
}