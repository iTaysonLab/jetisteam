package bruhcollective.itaysonlab.cobalt.news

import bruhcollective.itaysonlab.cobalt.news.discover.DiscoverComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface NewsRootComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Discover(val component: DiscoverComponent) : Child()
    }
}