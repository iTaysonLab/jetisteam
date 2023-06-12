package bruhcollective.itaysonlab.jetisteam.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.news.NewsScreen
import bruhcollective.itaysonlab.jetisteam.ui.components.CobaltNavigationBar
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children

@Composable
fun CobaltContainerScreen(
    isConnectionRowShown: Boolean,
    component: CobaltContainerComponent
) {
    Scaffold(
        bottomBar = {
            CobaltNavigationBar()
        }
    ) { innerPadding ->
        val stackTopPadding by animateDpAsState(
            targetValue = if (isConnectionRowShown) {
                0.dp
            } else {
                innerPadding.calculateTopPadding()
            }, label = "Cobalt container status bar neutralizer"
        )

        Children(stack = component.childStack, modifier = Modifier.padding(top = stackTopPadding, bottom = 0.dp)) {
            when (val child = it.instance) {
                is CobaltContainerComponent.Child.Home -> NewsScreen(child.component)
            }
        }
    }
}