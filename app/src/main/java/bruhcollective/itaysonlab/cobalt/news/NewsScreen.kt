package bruhcollective.itaysonlab.cobalt.news

import androidx.compose.runtime.Composable
import bruhcollective.itaysonlab.cobalt.news.discover.DiscoverScreen
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