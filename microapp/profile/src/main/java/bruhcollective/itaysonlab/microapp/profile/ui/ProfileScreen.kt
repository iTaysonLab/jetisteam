package bruhcollective.itaysonlab.microapp.profile.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import bruhcollective.itaysonlab.jetisteam.uikit.LocalSteamTheme
import bruhcollective.itaysonlab.jetisteam.uikit.SteamColors
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.ksteam.models.apps.AppSummary
import bruhcollective.itaysonlab.ksteam.models.apps.pageBackground
import bruhcollective.itaysonlab.ksteam.util.RichPresenceFormatter
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.delegates.LocalShareDispatcher
import bruhcollective.itaysonlab.microapp.profile.ui.components.ProfileHeader
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

    /*InstallTypedResultHandler<ProfileEditEvent>(backStackEntry) { event ->
        viewModel.consumeEvent(event)
    }*/

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        val profileSummary =
            data.personaSummaryState.collectAsStateWithLifecycle(context = Dispatchers.IO)
        val profile = data.personaState.collectAsStateWithLifecycle(context = Dispatchers.IO)

        val theme = remember(data.customization.profileTheme.themeId) {
            SteamColors.getColorTheme(data.customization.profileTheme.themeId)
        }

        var currentGameSummary by remember {
            mutableStateOf<AppSummary?>(null)
        }

        var currentGameRichPresence by remember {
            mutableStateOf<RichPresenceFormatter.FormattedRichPresence?>(null)
        }

        LaunchedEffect(profile.value.ingameAppId, profile.value.ingameRichPresence) {
            currentGameSummary = if (profile.value.ingameAppId.id != 0) {
                viewModel.getAppSummary(profile.value.ingameAppId)
            } else {
                null
            }

            currentGameRichPresence =
                if (profile.value.ingameAppId.id != 0 && profile.value.ingameRichPresence.isNotEmpty()) {
                    RichPresenceFormatter.formatRp(
                        steamRp = profile.value.ingameRichPresence,
                        localizationTokens = viewModel.getLocalizationForRichPresence(profile.value.ingameAppId)
                    )
                } else {
                    null
                }
        }

        DisposableEffect(systemUiController, isDarkTheme) {
            systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = false)

            onDispose {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = isDarkTheme.not()
                )
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

                            Button(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = cardBackground,
                                    contentColor = cardContent
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Wallet,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Balance")
                            }
                        } else {
                            val textAlpha by animateFloatAsState(
                                targetValue = if (tas.state.overlappedFraction > 0.01f) 1f else 0f,
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            )

                            Text(text = profile.value.name, Modifier.alpha(textAlpha))
                        }
                    }, scrollBehavior = tas, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            3.dp
                        ),
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                    ), actions = {
                        IconButton(onClick = {
                            share.share(
                                url = profileSummary.value.profileUrl,
                                humanReadableTitle = profile.value.name,
                                pictureUrl = profile.value.avatar.full
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.IosShare,
                                contentDescription = null
                            )
                        }
                    }, navigationIcon = {
                        IconButton(onClick = {
                            onNavigationClick(
                                viewModel.isRootScreen,
                                viewModel.steamId.longId
                            )
                        }) {
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
                            backgroundUrl = data.equipment.background?.imageLarge?.url,
                            avatarUrl = data.equipment.animatedAvatar?.imageLarge?.url
                                ?: profile.value.avatar.full,
                            avatarFrameUrl = data.equipment.avatarFrame?.imageLarge?.url,
                            profileName = profile.value.name,
                            onLibraryClick = { onLibraryClick(viewModel.steamId.longId) },
                            onFriendsClick = { onFriendsClick(viewModel.steamId.longId) }
                        )
                    }

                    if (currentGameSummary != null) {
                        item {
                            Card(onClick = { /*TODO*/ }, modifier = Modifier.padding(16.dp)) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min)
                                ) {

                                    AsyncImage(
                                        model = currentGameSummary?.pageBackground?.url,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize().drawWithContent {
                                            drawContent()
                                            drawRect(Color.Black.copy(alpha = 0.5f))
                                        },
                                        contentScale = ContentScale.Crop
                                    )

                                    Column(Modifier.padding(16.dp)) {
                                        Text(text = currentGameSummary?.name.orEmpty())
                                        Text(text = currentGameRichPresence?.formattedString.orEmpty())
                                        Text(text = "Playing with ${currentGameRichPresence?.groupSize} players")
                                    }

                                }
                            }
                        }
                    }

                    /*items(data.customization.profileCustomizationEntries) { entry ->
                        ProfileCardEntry(
                            entry = entry,
                            onGameClick = onGameClick,
                            getGame = viewModel::game,
                            getGameWithAchievements = viewModel::gameToAchievements,
                            getGameSize = viewModel::gameSize
                        )
                    }*/
                }
            }
        }
    }
}