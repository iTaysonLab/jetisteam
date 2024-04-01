package bruhcollective.itaysonlab.jetisteam.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Feed
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.sharp.Feed
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material.icons.sharp.Security
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.guard.GuardScreen
import bruhcollective.itaysonlab.jetisteam.news.NewsScreen
import bruhcollective.itaysonlab.jetisteam.profile.ProfileScreen
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.components.IslandAnimations
import bruhcollective.itaysonlab.jetisteam.ui.components.SteamConnectionRow
import bruhcollective.itaysonlab.jetisteam.ui.rememberPrevious
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimator
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import soup.compose.material.motion.MotionConstants

@Composable
fun CobaltContainerScreen(
    component: CobaltContainerComponent
) {
    val currentNavItem by component.currentNavigationItem.subscribeAsState()
    val previousNavItem = rememberPrevious(current = currentNavItem)

    Scaffold(
        bottomBar = {
            val navbarItems by component.navigationItems.subscribeAsState()
            val steamConnectionState by component.connectionStatus.collectAsStateWithLifecycle()

            Column {
                SteamConnectionRow(
                    connectionState = steamConnectionState
                )

                NavigationBar {
                    navbarItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentNavItem == item,
                            onClick = {
                                component.switch(item)
                            },
                            icon = {
                                Icon(
                                    imageVector = when (item) {
                                        CobaltContainerComponent.NavigationItem.Home -> Icons.AutoMirrored.Rounded.Feed
                                        CobaltContainerComponent.NavigationItem.MyProfile -> Icons.Rounded.Person
                                        CobaltContainerComponent.NavigationItem.Guard -> Icons.Rounded.Security
                                    },
                                    contentDescription = null,
                                )
                            }, label = {
                                Text(text = stringResource(
                                    id = when (item) {
                                        CobaltContainerComponent.NavigationItem.Home -> R.string.tab_news
                                        CobaltContainerComponent.NavigationItem.MyProfile -> R.string.tab_profile
                                        CobaltContainerComponent.NavigationItem.Guard -> R.string.tab_guard
                                    }
                                ))
                            }
                        )
                    }
                }
            }
        }, contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        Children(stack = component.childStack, animation = stackAnimation { _ ->
            val spec: FiniteAnimationSpec<Float> = tween(durationMillis = MotionConstants.DefaultMotionDuration, easing = FastOutSlowInEasing)

            val direction = if (previousNavItem != null && component.getNavigationItemIndex(currentNavItem) > component.getNavigationItemIndex(previousNavItem)) {
                IslandAnimations.Direction.LEFT
            } else {
                IslandAnimations.Direction.RIGHT
            }

            fade(spec) + slideWithDirection(direction, spec)
        }, modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            when (val child = it.instance) {
                is CobaltContainerComponent.Child.Home -> NewsScreen(child.component)
                is CobaltContainerComponent.Child.MyProfile -> ProfileScreen(child.component)
                is CobaltContainerComponent.Child.Guard -> GuardScreen(child.component)
            }
        }
    }
}

private fun slideWithDirection(
    direction: IslandAnimations.Direction,
    animationSpec: FiniteAnimationSpec<Float>
): StackAnimator = stackAnimator(animationSpec) { factor, _, content ->
    content(Modifier.offsetXFactor(factor * if (direction == IslandAnimations.Direction.RIGHT) 1f else -1f))
}

private fun Modifier.offsetXFactor(factor: Float): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(x = (placeable.width.toFloat() * factor).toInt(), y = 0)
        }
    }