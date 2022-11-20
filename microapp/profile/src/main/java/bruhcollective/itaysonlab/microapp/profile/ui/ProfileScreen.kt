package bruhcollective.itaysonlab.microapp.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.SteamColors
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.profile.ui.components.ProfileCardEntry
import bruhcollective.itaysonlab.microapp.profile.ui.components.ProfileHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileScreen(
    onGameClick: (Int) -> Unit,
    onLibraryClick: (Long) -> Unit,
    onFriendsClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onNavigationClick: (Boolean) -> Unit,
    reloadFlag: Boolean,
    onReloadFlagTriggered: () -> Unit,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val tab = rememberTopAppBarState()
    val tas = TopAppBarDefaults.pinnedScrollBehavior(state = tab)

    LaunchedEffect(reloadFlag) {
        if (reloadFlag) {
            viewModel.reload()
            onReloadFlagTriggered()
        }
    }

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        val theme = remember(data.customization.profileTheme.themeId) {
            SteamColors.getColorTheme(data.customization.profileTheme.themeId)
        }

        CompositionLocalProvider(LocalSteamTheme provides theme) {
            Scaffold(topBar = {
                TopAppBar(title = {

                }, scrollBehavior = tas, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    actionIconContentColor = Color.White,
                ), actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Rounded.IosShare, contentDescription = null)
                    }
                }, navigationIcon = {
                    IconButton(onClick = { onNavigationClick(viewModel.isRootScreen) }) {
                        Icon(imageVector = if (viewModel.isRootScreen) {
                            Icons.Rounded.Menu
                        } else {
                            Icons.Rounded.ArrowBack
                        }, contentDescription = null)
                    }
                })
            }, modifier = Modifier
                .nestedScroll(tas.nestedScrollConnection)
                .fillMaxSize()) {
                LazyColumn(modifier = Modifier
                    .background(theme.gradientBackground.copy(alpha = 1f))
                    .fillMaxSize()) {
                    item {
                        ProfileHeader(
                            backgroundUrl = data.equipment.background?.imageLarge,
                            avatarUrl = data.playerProfile.avatarfull,
                            avatarFrameUrl = data.equipment.avatarFrame?.imageLarge,
                            profile = data.playerProfile,
                            summary = data.summary,
                            onLibraryClick = { onLibraryClick(viewModel.steamId.steamId) },
                            onFriendsClick = { onFriendsClick(viewModel.steamId.steamId) },
                            onEditClick = { onEditClick(viewModel.steamId.steamId) }
                        )
                    }

                    items(data.customization.profileCustomizationEntries) { entry ->
                        ProfileCardEntry(
                            entry = entry,
                            onGameClick = onGameClick,
                            getGame = viewModel::game,
                            getGameWithAchievements = viewModel::gameToAchievements,
                            getGameSize = viewModel::gameSize
                        )
                    }
                }
            }
        }
    }
}