package bruhcollective.itaysonlab.cobalt.navigation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.twotone.Feed
import androidx.compose.material.icons.automirrored.twotone.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.navigation.implementations.RootDestination
import bruhcollective.itaysonlab.cobalt.navigation.implementations.RootNavigationComponent
import bruhcollective.itaysonlab.cobalt.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.cobalt.ui.components.IslandAnimations
import bruhcollective.itaysonlab.cobalt.ui.components.SteamConnectionRow
import bruhcollective.itaysonlab.cobalt.ui.rememberPrevious
import bruhcollective.itaysonlab.ksteam.network.CMClientState
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimator
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun RootNavigationScreen(
    component: RootNavigationComponent,
    steamConnectionStatus: CMClientState,
) {
    val children by component.stack.subscribeAsState()
    val bottomBarItems by component.availableRootDestinations.subscribeAsState()

    val currentNavItem = children.active.configuration
    val previousNavItem = rememberPrevious(current = currentNavItem)

    Scaffold(
        bottomBar = {
            Column {
                SteamConnectionRow(connectionState = steamConnectionStatus)

                NavigationBar {
                    for (barItem in bottomBarItems) {
                        val (deselectedIcon, selectedIcon, textResId) = when (barItem) {
                            RootDestination.Newsfeed -> Triple(Icons.AutoMirrored.TwoTone.Feed, Icons.AutoMirrored.Filled.Feed, R.string.tab_news)
                            RootDestination.Profile -> Triple(Icons.TwoTone.Person, Icons.Filled.Person, R.string.tab_profile)
                            RootDestination.Guard -> Triple(Icons.TwoTone.Security, Icons.Filled.Security, R.string.tab_guard)
                            RootDestination.Library -> Triple(Icons.AutoMirrored.TwoTone.LibraryBooks, Icons.AutoMirrored.Filled.LibraryBooks, R.string.tab_library)
                        }

                        NavigationBarItem(
                            selected = currentNavItem == barItem,
                            onClick = {
                                component.selectRootDestination(barItem)
                                      },
                            icon = {
                                Icon(
                                    imageVector = if (currentNavItem == barItem) selectedIcon else deselectedIcon,
                                    contentDescription = stringResource(id = textResId),
                                )
                            }, label = {
                                Text(text = stringResource(id = textResId))
                            }
                        )
                    }
                }
            }
        }, contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        Children(stack = children, animation = stackAnimation { _ ->
            val spec = spring<Float>(
                stiffness = 500f
            )

            val direction =
                if (previousNavItem != null && bottomBarItems.indexOf(currentNavItem) > bottomBarItems.indexOf(previousNavItem)) {
                    IslandAnimations.Direction.LEFT
                } else {
                    IslandAnimations.Direction.RIGHT
                }

            fade(spec) + slideWithDirection(direction, spec)
        }, modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            DestinationScreen(component = it.instance)
        }
    }
}

private fun slideWithDirection(
    direction: IslandAnimations.Direction,
    animationSpec: FiniteAnimationSpec<Float>
): StackAnimator = stackAnimator(animationSpec) { factor, dcDirection, content ->
    if ((direction == IslandAnimations.Direction.RIGHT && dcDirection == Direction.ENTER_FRONT) || (direction == IslandAnimations.Direction.LEFT && dcDirection == Direction.EXIT_BACK)) {
        content(Modifier.offsetXFactor(factor * -1f))
    } else if ((direction == IslandAnimations.Direction.LEFT && dcDirection == Direction.ENTER_FRONT) || (direction == IslandAnimations.Direction.RIGHT && dcDirection == Direction.EXIT_BACK)) {
        content(Modifier.offsetXFactor(factor * 1f))
    } else {
        // Log.d("CCS", "SWD -> $direction | ${dcDirection}")
        content(Modifier.offsetXFactor(factor * -1f))
    }
}

private fun Modifier.offsetXFactor(factor: Float): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(x = (placeable.width.toFloat() * factor).toInt(), y = 0)
        }
    }