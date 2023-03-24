package bruhcollective.itaysonlab.microapp.profile.ui.screens.edit

/*import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenError
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.InstallTypedResultHandler
import bruhcollective.itaysonlab.microapp.profile.R
import bruhcollective.itaysonlab.microapp.profile.core.ProfileEditEvent
import bruhcollective.itaysonlab.microapp.profile.core.SectionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileEditScreen(
    viewModel: ProfileEditViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onSectionNavigate: (Long, SectionType) -> Unit,
    backStackEntry: NavBackStackEntry,
    onNavResultConsumed: (ProfileEditEvent) -> Unit
) {
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    InstallTypedResultHandler<ProfileEditEvent>(backStackEntry) { event ->
        viewModel.consumeEvent(event)
        onNavResultConsumed(event)
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = stringResource(id = R.string.edit_title))
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
                is PageViewModel.State.Error -> FullscreenError(
                    onReload = viewModel::reload,
                    exception = state.exception
                )

                is PageViewModel.State.Loading -> FullscreenLoading()

                is PageViewModel.State.Loaded -> {
                    LazyColumn {
                        items(viewModel.uiSections) { block ->
                            ListItem(
                                headlineContent = {
                                    Text(text = stringResource(block.title))
                                }, supportingContent = {
                                    Text(
                                        text = block.text ?: stringResource(id = R.string.edit_default)
                                    )
                                }, leadingContent = {
                                    Icon(imageVector = block.icon(), contentDescription = null)
                                }, colors = ListItemDefaults.colors(
                                    containerColor = Color.Transparent
                                ), modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSectionNavigate(viewModel.steamId.steamId, block.type)
                                    }
                            )

                            Divider(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}*/