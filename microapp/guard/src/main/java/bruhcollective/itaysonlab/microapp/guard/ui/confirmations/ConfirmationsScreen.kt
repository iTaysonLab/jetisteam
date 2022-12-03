package bruhcollective.itaysonlab.microapp.guard.ui.confirmations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import bruhcollective.itaysonlab.jetisteam.models.MobileConfirmationItem
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenError
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.util.DateUtil
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.InstallTypedResultHandler
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.ui.confirmation_detail.ConfirmationDetailViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConfirmationsScreen(
    viewModel: ConfirmationsViewModel = hiltViewModel(),
    backStackEntry: NavBackStackEntry,
    onBackClicked: () -> Unit,
    onConfirmationClicked: (Long, String) -> Unit
) {
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    InstallTypedResultHandler<ConfirmationDetailViewModel.ConfirmationDetailResult>(backStackEntry) { event ->
        viewModel.consumeEvent(event)
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = stringResource(id = R.string.guard_confirmations))
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
        RoundedPage(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (val state = viewModel.state) {
                is PageViewModel.State.Loading -> FullscreenLoading()
                is PageViewModel.State.Error -> FullscreenError(
                    onReload = viewModel::reload,
                    exception = state.exception
                )
                is PageViewModel.State.Loaded -> {
                    if (state.data.success) {
                        if (state.data.conf.orEmpty().isNotEmpty()) {
                            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(state.data.conf.orEmpty()) {
                                    ConfirmationCard(confirmation = it, onClick = {
                                        onConfirmationClicked(viewModel.steamId.steamId, viewModel.wrapConfirmation(it))
                                    })
                                }
                            }
                        } else {
                            FullscreenPlaceholder(
                                title = stringResource(id = R.string.guard_confirmations_empty),
                                text = stringResource(id = R.string.guard_confirmations_empty_desc),
                            )
                        }
                    } else {
                        FullscreenPlaceholder(
                            title = state.data.message.orEmpty(),
                            text = state.data.detail.orEmpty(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FullscreenPlaceholder(
    title: String,
    text: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ConfirmationCard(
    confirmation: MobileConfirmationItem,
    onClick: () -> Unit
) {
    val formattedDate = remember(confirmation.creationTime) {
        DateUtil.formatDateTimeToLocale(confirmation.creationTime * 1000L)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = confirmation.icon,
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .size(64.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
            )

            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = confirmation.typeName, fontSize = 13.sp)
                    Text(text = formattedDate, modifier = Modifier.alpha(0.7f), fontSize = 13.sp)
                }

                Text(
                    text = confirmation.headline,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(text = remember(confirmation.summary) {
                    confirmation.summary.joinToString(separator = "\n")
                }, fontSize = 13.sp, lineHeight = 18.sp)
            }
        }
    }
}