package bruhcollective.itaysonlab.jetisteam.ui.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.microapp.auth.AuthMicroapp
import bruhcollective.itaysonlab.microapp.core.*
import bruhcollective.itaysonlab.microapp.core.ext.ROOT_NAV_GRAPH_ID
import bruhcollective.itaysonlab.microapp.core.ext.navigateRoot
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
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

    LaunchedEffect(Unit) {
        if (navController.currentDestination?.route != "coreLoading") return@LaunchedEffect

        val startRoute = if (viewModel.signedIn()) {
            viewModel.destinations.find<GuardMicroapp>()
        } else {
            viewModel.destinations.find<AuthMicroapp>()
        }.microappRoute

        navController.navigateRoot(startRoute)
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

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = MaterialTheme.shapes.extraLarge.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)),
        scrimColor = MaterialTheme.colorScheme.scrim.copy(0.5f),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface
    ) {
        Scaffold(
            bottomBar = {
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
                        val selected = navController.backQueue.any {
                            it.destination.route?.startsWith(dest.route) == true
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
                            onClick = {
                                if (selected) return@NavigationBarItem
                                navController.navigate(dest.route) {
                                    popUpTo(ROOT_NAV_GRAPH_ID) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { padding ->
            AnimatedNavHost(
                navController = navController,
                startDestination = "coreLoading",
                route = ROOT_NAV_GRAPH_ID,
                modifier = Modifier.padding(bottom = navOffsetReverse),
                enterTransition = {
                    viewModel.buildAnimation(this) { forwardDirection ->
                        materialSharedAxisXIn(forward = forwardDirection, slideDistance = slideDistance)
                    }
                },
                exitTransition = {
                    viewModel.buildAnimation(this) { forwardDirection ->
                        materialSharedAxisXOut(forward = forwardDirection, slideDistance = slideDistance)
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
                    // FullscreenLoading()
                }

                viewModel.destinations.forEach { (key, value) ->
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
    private val steamSessionController: SteamSessionController,
    val destinations: Destinations,
) : ViewModel() {
    val fullscreenDestinations = destinations
        .map { it.value.fullscreenRoutes }
        .flatten()
        .distinct()

    val bottomNavDestinations = listOf(
        destinations.find<NotificationsMicroapp>(),
        destinations.find<GuardMicroapp>(),
        destinations.find<ProfileMicroapp>(),
    ).map(BottomNavigationCapable::bottomNavigationEntry)

    fun signedIn() = steamSessionController.signedIn()
    fun mySteamId() = steamSessionController.steamId()

    @OptIn(ExperimentalAnimationApi::class)
    fun <T> buildAnimation(scope: AnimatedContentScope<NavBackStackEntry>, builder: (forwardDirection: Boolean) -> T): T {
        val isRoute = getStartingRoute(scope.initialState.destination)
        val tsRoute = getStartingRoute(scope.targetState.destination)

        val isIndex = bottomNavDestinations.indexOfFirst { it.route == isRoute }
        val tsIndex = bottomNavDestinations.indexOfFirst { it.route == tsRoute }

        return builder(
            isRoute == tsRoute || tsIndex > isIndex
        )
    }

    private fun getStartingRoute(destination: NavDestination): String {
        return destination.hierarchy.toList().let { it[it.lastIndex - 1] }.route.orEmpty()
    }
}