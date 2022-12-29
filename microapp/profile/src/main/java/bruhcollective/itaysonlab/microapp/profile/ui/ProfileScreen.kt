package bruhcollective.itaysonlab.microapp.profile.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import bruhcollective.itaysonlab.jetisteam.uikit.LocalSteamTheme
import bruhcollective.itaysonlab.jetisteam.uikit.SteamColors
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.delegates.LocalShareDispatcher
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.InstallTypedResultHandler
import bruhcollective.itaysonlab.microapp.profile.core.ProfileEditEvent
import bruhcollective.itaysonlab.microapp.profile.ui.components.ProfileCardEntry
import bruhcollective.itaysonlab.microapp.profile.ui.components.ProfileHeader
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileScreen(
    onGameClick: (Int) -> Unit,
    onLibraryClick: (Long) -> Unit,
    onFriendsClick: (Long) -> Unit,
    onNavigationClick: (Boolean, Long) -> Unit,
    backStackEntry: NavBackStackEntry,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()

    val tas = TopAppBarDefaults.pinnedScrollBehavior()
    val share = LocalShareDispatcher.current

    InstallTypedResultHandler<ProfileEditEvent>(backStackEntry) { event ->
        viewModel.consumeEvent(event)
    }

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        val theme = remember(data.customization.profileTheme.themeId) {
            SteamColors.getColorTheme(data.customization.profileTheme.themeId)
        }

        DisposableEffect(systemUiController, isDarkTheme) {
            systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = false)

            onDispose {
                systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = isDarkTheme.not())
            }
        }

        CompositionLocalProvider(LocalSteamTheme provides theme) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(title = {
                        if (viewModel.isRootScreen) {
                            val cardBackground by animateColorAsState(
                                targetValue = lerp(
                                    theme.btnBackground.copy(alpha = 0.5f),
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    if (tas.state.overlappedFraction > 0.01f) 1f else 0f
                                ),

                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            )

                            val cardContent by animateColorAsState(
                                targetValue = lerp(
                                    Color.White,
                                    MaterialTheme.colorScheme.onSurface,
                                    if (tas.state.overlappedFraction > 0.01f) 1f else 0f
                                ),

                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            )

                            Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                                containerColor = cardBackground,
                                contentColor = cardContent
                            ), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                                Icon(imageVector = Icons.Rounded.Wallet, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = data.summary?.walletBalance ?: "")
                            }
                        } else {
                            val textAlpha by animateFloatAsState(
                                targetValue = if (tas.state.overlappedFraction > 0.01f) 1f else 0f,
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            )

                            Text(text = data.playerProfile.personaname, Modifier.alpha(textAlpha))
                        }
                    }, scrollBehavior = tas, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                    ), actions = {
                        IconButton(onClick = {
                            share.share(
                                url = data.playerProfile.profileurl,
                                humanReadableTitle = data.playerProfile.personaname,
                                pictureUrl = data.playerProfile.avatarfull
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.IosShare,
                                contentDescription = null
                            )
                        }
                    }, navigationIcon = {
                        IconButton(onClick = { onNavigationClick(viewModel.isRootScreen, viewModel.steamId.steamId) }) {
                            Icon(
                                imageVector = if (viewModel.isRootScreen) {
                                    Icons.Rounded.Menu
                                } else {
                                    Icons.Rounded.ArrowBack
                                }, contentDescription = null
                            )
                        }
                    })
                }, modifier = Modifier
                    .nestedScroll(tas.nestedScrollConnection)
                    .fillMaxSize()
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .background(theme.gradientBackground.copy(alpha = 1f))
                        .fillMaxSize()
                ) {
                    item {
                        ProfileHeader(
                            backgroundUrl = data.equipment.background?.imageLarge,
                            avatarUrl = data.equipment.animatedAvatar?.imageLarge ?: data.playerProfile.avatarfull,
                            avatarFrameUrl = data.equipment.avatarFrame?.imageLarge,
                            profile = data.playerProfile,
                            summary = data.summary,
                            onLibraryClick = { onLibraryClick(viewModel.steamId.steamId) },
                            onFriendsClick = { onFriendsClick(viewModel.steamId.steamId) }
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