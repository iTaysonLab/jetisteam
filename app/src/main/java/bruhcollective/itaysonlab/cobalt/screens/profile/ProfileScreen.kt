package bruhcollective.itaysonlab.cobalt.screens.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    component: ProfileComponent
) {
    val widgets by component.widgetsComponent.widgets.subscribeAsState()
    val personaTitle by component.headerComponent.title.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(personaTitle)
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
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