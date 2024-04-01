package bruhcollective.itaysonlab.jetisteam.news

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import bruhcollective.itaysonlab.cobalt.news.NewsRootComponent
import bruhcollective.itaysonlab.jetisteam.news.discover.DiscoverScreen
import com.arkivanov.decompose.extensions.compose.stack.Children

@Composable
fun NewsScreen(
    component: NewsRootComponent,
) {
    Children(stack = component.childStack) {
        when (val child = it.instance) {
            is NewsRootComponent.Child.Discover -> DiscoverScreen(component = child.component)
        }
    }
}