package bruhcollective.itaysonlab.microapp.profile.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.LocalFloatingBarInset
import bruhcollective.itaysonlab.jetisteam.uikit.LocalSteamTheme
import bruhcollective.itaysonlab.jetisteam.uikit.SteamColors
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.ksteam.models.enums.EFriendRelationship
import bruhcollective.itaysonlab.ksteam.models.enums.isFriend
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileCustomization
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget
import bruhcollective.itaysonlab.ksteam.models.persona.SummaryPersona
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.delegates.LocalShareDispatcher
import bruhcollective.itaysonlab.microapp.profile.ui.components.ProfileHeader
import bruhcollective.itaysonlab.microapp.profile.ui.components.ProfileWidgetBase
import bruhcollective.itaysonlab.microapp.profile.ui.profile_widgets.FavoriteGameWidget
import bruhcollective.itaysonlab.microapp.profile.ui.profile_widgets.GameCollectorWidget
import bruhcollective.itaysonlab.microapp.profile.ui.profile_widgets.NowPlayingWidget
import bruhcollective.itaysonlab.microapp.profile.ui.profile_widgets.UnknownWidget
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers

@Composable
internal fun ProfileScreen(
    onGameClick: (Int) -> Unit,
    onFriendsClick: (Long) -> Unit,
    onNavigationClick: (Boolean, Long) -> Unit,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
    val share = LocalShareDispatcher.current

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        val profileSummary = data.personaSummaryState.collectAsStateWithLifecycle(context = Dispatchers.IO)
        val profile = data.personaState.collectAsStateWithLifecycle(context = Dispatchers.IO)
        val relationship = data.relationship.collectAsStateWithLifecycle(context = Dispatchers.IO)

        DisposableEffect(systemUiController, isDarkTheme) {
            systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = false)

            onDispose {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = isDarkTheme.not()
                )
            }
        }

        LaunchedEffect(profile.value.ingameAppId, profile.value.ingameRichPresence) {
            viewModel.updateRichPresenceData(profile.value)
        }

        ProfileScreenImpl(
            persona = profile.value,
            personaRelationship = relationship.value,
            personaSummary = profileSummary.value,
            equipment = data.equipment,
            customization = data.customization,
            isRootScreen = viewModel.isRootScreen,
            richPresence = viewModel.richPresenceInformation,
            onNavigationClick = {
                onNavigationClick(viewModel.isRootScreen, viewModel.steamId.longId)
            }, onShareClick = {
                profileSummary.value.let { summary ->
                    share.share(
                        url = summary.profileUrl,
                        humanReadableTitle = summary.name,
                        pictureUrl = summary.avatar.full
                    )
                }
            }
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenImpl(
    persona: Persona,
    personaRelationship: EFriendRelationship,
    personaSummary: SummaryPersona,
    equipment: ProfileEquipment,
    customization: ProfileCustomization,
    isRootScreen: Boolean,
    richPresence: ProfileScreenViewModel.RichPresence?,
    onNavigationClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    val tas = TopAppBarDefaults.pinnedScrollBehavior()

    val theme = remember(customization.profileTheme) {
        SteamColors.getColorTheme(customization.profileTheme?.themeId)
    }

    val profileName = if (personaRelationship.isFriend) {
        persona.name
    } else {
        personaSummary.name
    }

    val profileAvatar = equipment.animatedAvatar?.imageLarge?.url ?: (
            if (personaRelationship.isFriend) {
                persona.avatar.full
            } else {
                personaSummary.avatar.full
            }
    )

    val profileState = if (personaRelationship.isFriend) {
        persona.onlineStatus
    } else {
        personaSummary.onlineStatus
    }

    val profileLastSeen = if (personaRelationship.isFriend) {
        persona.lastSeen
    } else {
        personaSummary.lastSeen
    }

    CompositionLocalProvider(LocalSteamTheme provides theme) {
        Scaffold(
            topBar = {
                ProfileToolbar(
                    showBalance = isRootScreen,
                    scrollBehavior = tas,
                    onNavigationClick = onNavigationClick,
                    onShareClick = onShareClick,
                    profileName = persona.name
                )
            }, modifier = Modifier
                .nestedScroll(tas.nestedScrollConnection)
                .fillMaxSize()
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .background(theme.gradientBackground.copy(alpha = 1f))
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = LocalFloatingBarInset.current + 16.dp)
            ) {
                item {
                    ProfileHeader(
                        backgroundUrl = equipment.background?.imageLarge?.url,
                        avatarUrl = profileAvatar,
                        avatarFrameUrl = equipment.avatarFrame?.imageLarge?.url,
                        profileName = profileName,
                        relationship = personaRelationship,
                        personaState = profileState,
                        onLibraryClick = { /* onLibraryClick(viewModel.steamId.longId) */ },
                        onFriendsClick = { /* onFriendsClick(viewModel.steamId.longId) */ },
                        lastSeen = profileLastSeen
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (richPresence != null) {
                    item {
                        NowPlayingWidget(richPresence)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                itemsIndexed(customization.profileWidgets) { index, entry ->
                    ProfileWidgetBase(
                        firstItem = index == 0,
                        lastItem = index == customization.profileWidgets.lastIndex,
                        onClick = {

                        }
                    ) {
                        when (entry) {
                            is ProfileWidget.FavoriteGame -> {
                                FavoriteGameWidget(entry)
                            }

                            is ProfileWidget.GameCollector -> {
                                GameCollectorWidget(entry)
                            }

                            is ProfileWidget.Unknown -> {
                                UnknownWidget(entry)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileToolbar(
    modifier: Modifier = Modifier,
    showBalance: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationClick: () -> Unit,
    onShareClick: () -> Unit,
    profileName: String
) {
    CenterAlignedTopAppBar(title = {
        if (false && showBalance) {
            val cardBackground by animateColorAsState(
                targetValue = lerp(
                    LocalSteamTheme.current.btnBackground.copy(alpha = 0.5f),
                    MaterialTheme.colorScheme.surfaceVariant,
                    if (scrollBehavior.state.overlappedFraction > 0.01f) 1f else 0f
                ),

                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )

            val cardContent by animateColorAsState(
                targetValue = lerp(
                    Color.White,
                    MaterialTheme.colorScheme.onSurface,
                    if (scrollBehavior.state.overlappedFraction > 0.01f) 1f else 0f
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
                targetValue = if (scrollBehavior.state.overlappedFraction > 0.01f) 1f else 0f,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )

            Text(text = profileName, Modifier.alpha(textAlpha))
        }
    }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
            3.dp
        ),
        actionIconContentColor = Color.White,
        navigationIconContentColor = Color.White,
    ), actions = {
        IconButton(onClick = onShareClick) {
            Icon(
                imageVector = Icons.Rounded.IosShare,
                contentDescription = null
            )
        }
    }, navigationIcon = {
        IconButton(onClick = {
            onNavigationClick()
        }) {
            Icon(
                imageVector = if (showBalance) {
                    Icons.Rounded.Menu
                } else {
                    Icons.Rounded.ArrowBack
                }, contentDescription = null
            )
        }
    }, modifier = modifier)
}