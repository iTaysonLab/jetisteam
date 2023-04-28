package bruhcollective.itaysonlab.jetisteam.ui.screens

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.ui.SteamConnectionRow
import bruhcollective.itaysonlab.jetisteam.ui.SteamConnectionRowVisibility
import bruhcollective.itaysonlab.jetisteam.uikit.LocalFloatingBarInset
import bruhcollective.itaysonlab.jetisteam.uikit.components.bottom_bar.BottomBarItem
import bruhcollective.itaysonlab.jetisteam.uikit.components.bottom_bar.FloatingBottomBar
import bruhcollective.itaysonlab.ksteam.handlers.account
import bruhcollective.itaysonlab.ksteam.network.CMClientState
import bruhcollective.itaysonlab.microapp.auth.AuthMicroapp
import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.HasFullscreenRoutes
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.core.ext.ROOT_NAV_GRAPH_ID
import bruhcollective.itaysonlab.microapp.core.ext.navigateRoot
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import bruhcollective.itaysonlab.microapp.notifications.NotificationsMicroapp
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroapp
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import soup.compose.material.motion.animation.materialSharedAxisXIn
import soup.compose.material.motion.animation.materialSharedAxisXOut
import soup.compose.material.motion.animation.rememberSlideDistance
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun AppNavigation(
    viewModel: AppNavigationViewModel = hiltViewModel()
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navBackStack by navController.currentBackStack.collectAsStateWithLifecycle()
    val navCurrentRootNode = navBackStack.getOrNull(1)?.destination

    LaunchedEffect(Unit) {
        if (navController.currentDestination?.route != "coreLoading") return@LaunchedEffect
        navController.navigateRoot(viewModel.awaitSignInAndReturnDestination())
    }

    val shouldHideNavigationBar = remember(navBackStackEntry) {
        viewModel.fullscreenDestinations.any {
            it == navBackStackEntry?.destination?.route
        }
    }

    val navBarHeightDp = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val navOffset by animateDpAsState(if (shouldHideNavigationBar) 80.dp + navBarHeightDp else 0.dp)
    val navOffsetReverse by animateDpAsState(if (!shouldHideNavigationBar) 80.dp + navBarHeightDp else 0.dp)
    val slideDistance = rememberSlideDistance()

    val connectionState by viewModel.connectingState.collectAsStateWithLifecycle()

    var connectionRowVisible by remember { mutableStateOf(true) }

    val connectionRowPaddingCompensation by animateDpAsState(targetValue = if (connectionRowVisible) WindowInsets.statusBars.asPaddingValues().calculateTopPadding() else 0.dp, animationSpec = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = Dp.VisibilityThreshold
    ))

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = MaterialTheme.shapes.extraLarge.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)),
        scrimColor = MaterialTheme.colorScheme.scrim.copy(0.5f),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface
    ) {
        Scaffold(
            topBar = {
                SteamConnectionRow(connectionState, onVisibilityChanged = {
                    connectionRowVisible = it
                }, overrideVisibility = if (navCurrentRootNode?.route == "@auth") {
                    SteamConnectionRowVisibility.AlwaysHide
                } else {
                    SteamConnectionRowVisibility.Default
                })
            },
            bottomBar = {
                fun navigateTo(dest: NavigationEntry) {
                    navController.navigate(dest.route) {
                        popUpTo(ROOT_NAV_GRAPH_ID) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }

                val bottomBarItems = remember(connectionState, viewModel.bottomNavDestinations) {
                    viewModel.bottomNavDestinations.filter { dest ->
                        dest.route == "@guard" || connectionState == CMClientState.Connected
                    }.map { entry ->
                        BottomBarItem.Icon(
                            icon = entry.icon,
                            description = entry.name,
                            id = entry.route,
                            onClick = {
                                navigateTo(entry)
                            }
                        )
                    }
                }

                val selectedItemIndex = remember(bottomBarItems, navCurrentRootNode, connectionState) {
                    bottomBarItems.indexOfFirst { dest ->
                        navCurrentRootNode?.route == dest.id
                    }.coerceAtLeast(0)
                }

                FloatingBottomBar(
                    expanded = false,
                    selectedItem = selectedItemIndex,
                    items = bottomBarItems,
                    modifier = Modifier.offset {
                        IntOffset(
                            0,
                            navOffset
                                .toPx()
                                .toInt()
                        )
                    }, expandedContent = {}
                )
            }, contentWindowInsets = EmptyWindowInsets
        ) { padding ->
            CompositionLocalProvider(LocalFloatingBarInset provides navOffsetReverse) {
                AnimatedNavHost(
                    navController = navController,
                    startDestination = "coreLoading",
                    route = ROOT_NAV_GRAPH_ID,
                    modifier = Modifier
                        .padding(top = (padding.calculateTopPadding() - connectionRowPaddingCompensation).let {
                            if (it.value < 0f) {
                                0.dp
                            } else {
                                it
                            }
                        }),
                    enterTransition = {
                        if (initialState.destination.route == "coreLoading") {
                            EnterTransition.None
                        } else {
                            viewModel.buildAnimation(this) { forwardDirection ->
                                materialSharedAxisXIn(forward = forwardDirection, slideDistance = slideDistance)
                            }
                        }
                    },
                    exitTransition = {
                        if (initialState.destination.route == "coreLoading") {
                            ExitTransition.None
                        } else {
                            viewModel.buildAnimation(this) { forwardDirection ->
                                materialSharedAxisXOut(forward = forwardDirection, slideDistance = slideDistance)
                            }
                        }
                    },
                    popEnterTransition = {
                        materialSharedAxisXIn(forward = false, slideDistance = slideDistance)
                    },
                    popExitTransition = {
                        materialSharedAxisXOut(forward = false, slideDistance = slideDistance)
                    }
                ) {
                    composable("coreLoading") {
                        Box(modifier = Modifier.fillMaxSize())
                    }

                    viewModel.destinations.forEach { (_, value) ->
                        when (value) {
                            is ComposableMicroappEntry -> with(value) {
                                composable(
                                    navController,
                                    viewModel.destinations
                                )
                            }

                            is NestedMicroappEntry -> with(value) {
                                navigation(
                                    navController,
                                    viewModel.destinations
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@HiltViewModel
@JvmSuppressWildcards
class AppNavigationViewModel @Inject constructor(
    val destinations: Destinations,
    private val hostSteamClient: HostSteamClient
) : ViewModel() {
    val fullscreenDestinations = (destinations.values
        .filterIsInstance<HasFullscreenRoutes>()
        .map { it.fullscreenRoutes }
        .flatten() + "coreLoading").distinct()

    val bottomNavDestinations = listOf<BottomNavigationCapable>(
        //destinations.find<HomeMicroapp>(),
        destinations.find<NotificationsMicroapp>(),
        destinations.find<GuardMicroapp>(),
        destinations.find<LibraryMicroapp>(),
        destinations.find<ProfileMicroapp>(),
    ).map(BottomNavigationCapable::bottomNavigationEntry)

    val connectingState = hostSteamClient.client.connectionStatus

    fun awaitSignInAndReturnDestination(): String {
         return if (hostSteamClient.client.account.hasSavedDataForAtLeastOneAccount()) {
            // Then we should wait until it signs in
            // hostSteamClient.client.account.awaitSignIn()
            destinations.find<GuardMicroapp>().graphRoute
        } else {
            destinations.find<AuthMicroapp>().graphRoute
        }
    }

    fun <T> buildAnimation(scope: AnimatedContentTransitionScope<NavBackStackEntry>, builder: (forwardDirection: Boolean) -> T): T {
        val isRoute = getStartingRoute(scope.initialState.destination)
        val tsRoute = getStartingRoute(scope.targetState.destination)

        val isIndex = bottomNavDestinations.indexOfFirst { it.route == isRoute }
        val tsIndex = bottomNavDestinations.indexOfFirst { it.route == tsRoute }

        return builder(
            tsIndex == -1 || isRoute == tsRoute || tsIndex > isIndex
        )
    }

    private fun getStartingRoute(destination: NavDestination): String {
        return destination.hierarchy.toList().let { it[it.lastIndex - 1] }.route.orEmpty()
    }
}