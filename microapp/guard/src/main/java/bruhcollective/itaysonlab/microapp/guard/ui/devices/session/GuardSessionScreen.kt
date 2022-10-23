package bruhcollective.itaysonlab.microapp.guard.ui.devices.session

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardSessionScreen(
    viewModel: GuardSessionViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
) {
    val ctx = LocalContext.current
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = stringResource(id = R.string.guard_session_info))
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
        RoundedPage(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            LazyColumn {
                items(viewModel.infoBlocks) { info ->
                    ListItem(
                        headlineText = {
                            Text(text = stringResource(id = info.titleRes), maxLines = 1)
                        }, leadingContent = {
                            Icon(imageVector = info.icon(), contentDescription = null)
                        }, supportingText = {
                            Text(text = info.text)
                        }, colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )

                    Divider(color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.padding(start = 16.dp + 24.dp + 16.dp))
                }
            }
        }
    }
}