package bruhcollective.itaysonlab.microapp.steam_wrapped.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.LocalSteamTheme
import bruhcollective.itaysonlab.jetisteam.uikit.SteamColors
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.delegates.LocalShareDispatcher
import bruhcollective.itaysonlab.microapp.steam_wrapped.ui.blocks.ReplayHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SteamReplayScreen(
    viewModel: SteamReplayViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {
    val tas = TopAppBarDefaults.pinnedScrollBehavior()
    val share = LocalShareDispatcher.current

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        val theme = remember(data.customization.profileTheme.themeId) {
            SteamColors.getColorTheme(data.customization.profileTheme.themeId)
        }

        CompositionLocalProvider(LocalSteamTheme provides theme) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(title = {
                        val textAlpha by animateFloatAsState(
                            targetValue = if (tas.state.overlappedFraction > 0.01f) 1f else 0f,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                        )

                        Text(text = data.profile.personaname, Modifier.alpha(textAlpha))
                    }, scrollBehavior = tas, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                    ), actions = {
                        IconButton(onClick = {
                            share.share(
                                url = data.profile.profileurl,
                                humanReadableTitle = data.profile.personaname,
                                pictureUrl = data.profile.avatarfull
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.IosShare,
                                contentDescription = null
                            )
                        }
                    }, navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
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
                        ReplayHeader(
                            backgroundUrl = data.profileEquipment.background?.imageLarge,
                            avatarUrl = data.profileEquipment.animatedAvatar?.imageLarge ?: data.profile.avatarfull,
                            avatarFrameUrl = data.profileEquipment.avatarFrame?.imageLarge,
                            profile = data.profile,
                        )
                    }
                }
            }
        }
    }
}