package bruhcollective.itaysonlab.cobalt.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.profile.components.ProfileActionsStrip
import bruhcollective.itaysonlab.cobalt.profile.components.ProfileHeader
import bruhcollective.itaysonlab.cobalt.profile.components.ProfilePlayingCard
import bruhcollective.itaysonlab.cobalt.profile.widgets.ProfileWidgetPortal
import bruhcollective.itaysonlab.cobalt.ui.components.CobaltDivider
import bruhcollective.itaysonlab.cobalt.ui.components.FullscreenLoading
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun ProfileScreen(
    component: ProfileComponent
) {
    val state by component.state.subscribeAsState()

    LaunchedEffect(Unit) {
        component.dispatchComponentLoad()
    }

    when (state) {
        ProfileComponent.State.Idle, ProfileComponent.State.Loading -> {
            FullscreenLoading(modifier = Modifier.statusBarsPadding())
        }

        is ProfileComponent.State.Ready -> {
            ProfileScreenContent(component)
        }
    }
}

@Composable
private fun ProfileScreenContent(
    component: ProfileComponent
) {
    val collapsingScaffold = rememberCollapsingToolbarScaffoldState()
    val widgets by component.widgetsComponent.widgets.subscribeAsState()

    CollapsingToolbarScaffold(
        toolbar = {
            ProfileHeader(collapsingScaffold = collapsingScaffold, component = component.headerComponent)
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