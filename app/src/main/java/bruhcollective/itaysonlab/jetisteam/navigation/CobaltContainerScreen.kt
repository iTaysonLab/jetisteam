package bruhcollective.itaysonlab.jetisteam.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.news.NewsScreen
import bruhcollective.itaysonlab.jetisteam.profile.ProfileScreen
import bruhcollective.itaysonlab.jetisteam.ui.components.CobaltNavigationBar
import bruhcollective.itaysonlab.jetisteam.ui.components.IslandAnimations
import bruhcollective.itaysonlab.jetisteam.ui.rememberPrevious
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimator
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState

@Composable
fun CobaltContainerScreen(
    isConnectionRowShown: Boolean,
    component: CobaltContainerComponent
) {
    val currentNavItem by component.currentNavigationItem.subscribeAsState()
    val previousNavItem = rememberPrevious(current = currentNavItem)

    Scaffold(
        bottomBar = {
            val navbarItems by component.navigationItems.subscribeAsState()

            CobaltNavigationBar(
                items = navbarItems,
                selectedItem = currentNavItem,
                onSelected = component::switch
            )
        }
    ) { innerPadding ->
        val stackTopPadding by animateDpAsState(
            targetValue = if (isConnectionRowShown) {
                0.dp
            } else {
                innerPadding.calculateTopPadding()
            }, label = "Cobalt container status bar neutralizer"
        )

        Children(stack = component.childStack, animation = stackAnimation { _, _, _ ->
            val direction = if (previousNavItem != null && component.getNavigationItemIndex(currentNavItem) > component.getNavigationItemIndex(previousNavItem)) {
                IslandAnimations.Direction.RIGHT
            } else {
                IslandAnimations.Direction.LEFT
            }

            slideWithDirection(direction) + fade(IslandAnimations.islandSpec())
        }) {
            when (val child = it.instance) {
                is CobaltContainerComponent.Child.Home -> NewsScreen(stackTopPadding, child.component)
                is CobaltContainerComponent.Child.MyProfile -> ProfileScreen(stackTopPadding, child.component)
            }
        }
    }
}

private fun slideWithDirection(
    direction: IslandAnimations.Direction,
): StackAnimator = stackAnimator(IslandAnimations.islandSpec()) { factor, _, content ->
    content(Modifier.offsetXFactor(factor * if (direction == IslandAnimations.Direction.RIGHT) 1f else -1f))
}

private fun Modifier.offsetXFactor(factor: Float): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(x = (placeable.width.toFloat() * factor).toInt(), y = 0)
        }
    }