package bruhcollective.itaysonlab.jetisteam.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import bruhcollective.itaysonlab.ksteam.handlers.account
import bruhcollective.itaysonlab.ksteam.network.CMClientState
import bruhcollective.itaysonlab.microapp.auth.AuthMicroapp
import bruhcollective.itaysonlab.microapp.core.*
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.core.ext.ROOT_NAV_GRAPH_ID
import bruhcollective.itaysonlab.microapp.core.ext.navigateRoot
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import bruhcollective.itaysonlab.microapp.notifications.NotificationsMicroapp
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

    val connectionState = viewModel.connectingState.collectAsStateWithLifecycle()

    var connectionRowVisible by remember { mutableStateOf(true) }
    val connectionRowPadding by animateDpAsState(targetValue = if (connectionRowVisible) 56.dp else 0.dp, animationSpec = spring(
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
                SteamConnectionRow(connectionState.value, onVisibilityChanged = {
                    connectionRowVisible = it
                })
            },
            bottomBar = {
                val currentRootRoute = navController.backQueue.getOrNull(1)?.destination?.route

                NavigationBar(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                0,
                                navOffset
                                    .toPx()
                                    .toInt()
                            )
                        }
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                        .navigationBarsPadding(),
                ) {
                    viewModel.bottomNavDestinations.forEach { dest ->
                        val selected = currentRootRoute == dest.route

                        val onClick = remember(selected, navController, dest) {
                            {
                                if (!selected) {
                                    navController.navigate(dest.route) {
                                        popUpTo(ROOT_NAV_GRAPH_ID) {
                                            saveState = true
                                        }

                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    dest.icon(),
                                    contentDescription = stringResource(dest.name)
                                )
                            },
                            label = { Text(stringResource(dest.name)) },
                            selected = selected,
                            enabled = dest.route == "@guard" || connectionState.value == CMClientState.Connected,
                            onClick = onClick
                        )
                    }
                }
            }, contentWindowInsets = EmptyWindowInsets
        ) { padding ->
            AnimatedNavHost(
                navController = navController,
                startDestination = "coreLoading",
                route = ROOT_NAV_GRAPH_ID,
                modifier = Modifier
                    .padding(top = connectionRowPadding, bottom = navOffsetReverse),
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
        //destinations.find<ProfileMicroapp>(),
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

    @OptIn(ExperimentalAnimationApi::class)
    fun <T> buildAnimation(scope: AnimatedContentScope<NavBackStackEntry>, builder: (forwardDirection: Boolean) -> T): T {
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