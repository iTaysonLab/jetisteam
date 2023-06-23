package bruhcollective.itaysonlab.jetisteam.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.profile.ProfileComponent
import bruhcollective.itaysonlab.jetisteam.profile.components.ProfileActionsStrip
import bruhcollective.itaysonlab.jetisteam.profile.components.ProfileHeader
import bruhcollective.itaysonlab.jetisteam.profile.components.ProfilePlayingCard
import bruhcollective.itaysonlab.jetisteam.profile.widgets.ProfileWidgetPortal
import bruhcollective.itaysonlab.jetisteam.ui.components.CobaltDivider
import bruhcollective.itaysonlab.jetisteam.ui.components.FullscreenLoading
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun ProfileScreen(
    component: ProfileComponent,
    topPadding: Dp
) {
    val state by component.state.subscribeAsState()

    LaunchedEffect(Unit) {
        component.dispatchComponentLoad()
    }

    when (state) {
        ProfileComponent.State.Idle, ProfileComponent.State.Loading -> {
            FullscreenLoading(modifier = Modifier.padding(top = topPadding))
        }

        is ProfileComponent.State.Ready -> {
            ProfileScreenContent(topPadding, component)
        }
    }
}

@Composable
private fun ProfileScreenContent(
    topPadding: Dp,
    component: ProfileComponent
) {
    val collapsingScaffold = rememberCollapsingToolbarScaffoldState()
    val widgets by component.widgetsComponent.widgets.subscribeAsState()

    CollapsingToolbarScaffold(
        toolbar = {
            ProfileHeader(collapsingScaffold = collapsingScaffold, component = component.headerComponent, topPadding = topPadding)
        },
        state = collapsingScaffold,
        modifier = Modifier.fillMaxSize(),
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ProfileActionsStrip()
            }

            divider()

            item {
                ProfilePlayingCard(component.statusCardComponent)
            }

            divider()
            
            items(widgets) { widget ->
                ProfileWidgetPortal(widget)
                CobaltDivider(padding = 0.dp)
            }
        }
    }
}

private fun LazyListScope.divider() {
    item(contentType = 1) {
        CobaltDivider(padding = 0.dp)
    }
}