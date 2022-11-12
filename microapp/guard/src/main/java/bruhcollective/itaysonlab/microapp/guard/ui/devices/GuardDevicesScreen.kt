package bruhcollective.itaysonlab.microapp.guard.ui.devices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.utils.SessionFormatter
import steam.auth.CAuthentication_RefreshToken_Enumerate_Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardDevicesScreen(
    viewModel: GuardDevicesViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onSessionClicked: (Long, CAuthentication_RefreshToken_Enumerate_Response.RefreshTokenDescription) -> Unit
) {
    val ctx = LocalContext.current
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = stringResource(id = R.string.guard_actions_devices))
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
            .nestedScroll(tas.nestedScrollConnection)
    ) { innerPadding ->
        RoundedPage(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            when (val state = viewModel.state) {
                GuardDevicesViewModel.State.Loading -> FullscreenLoading()

                is GuardDevicesViewModel.State.Ready -> {
                    LazyColumn {
                        items(state.data) { session ->
                            val visuals = remember(session) { SessionFormatter.formatSessionDescByTime(ctx, session) }

                            ListItem(
                                headlineText = {
                                    Text(text = remember(session) {
                                        session.token_description.orEmpty().ifEmpty { visuals.fallbackName }
                                    }, maxLines = 1)
                                }, leadingContent = {
                                    Icon(imageVector = visuals.icon(), contentDescription = null)
                                }, supportingText = {
                                    Text(text = stringResource(id = R.string.guard_sessions_last_seen, visuals.relativeLastSeen), maxLines = 1)
                                }, colors = ListItemDefaults.colors(
                                    containerColor = Color.Transparent
                                ), modifier = Modifier.clickable {
                                    onSessionClicked(viewModel.steamId.steamId, session)
                                }
                            )

                            Divider(color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.padding(start = 16.dp + 24.dp + 16.dp))
                        }
                    }
                }
            }
        }
    }
}