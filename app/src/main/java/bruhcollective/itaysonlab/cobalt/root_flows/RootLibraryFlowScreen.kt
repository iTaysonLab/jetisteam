package bruhcollective.itaysonlab.cobalt.root_flows

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import bruhcollective.itaysonlab.cobalt.compose.LocalAnimatedVisibilityScope
import bruhcollective.itaysonlab.cobalt.compose.LocalNavSharedTransitionScope
import bruhcollective.itaysonlab.cobalt.library.LibraryScreen
import bruhcollective.itaysonlab.cobalt.published_files.PublishedFullscreenPhotoViewer
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation

@OptIn(ExperimentalDecomposeApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun RootLibraryFlowScreen(component: RootLibraryFlowComponent) {
     SharedTransitionScope { sharedTransitionModifier ->
        ChildStack(
            stack = component.stack,
            animation = stackAnimation(
                animator = fade() + scale(),
                predictiveBackParams = {
                    PredictiveBackParams(
                        backHandler = component.backHandler,
                        onBack = component::onBackClicked,
                    )
                }
            ), modifier = sharedTransitionModifier
        ) { child ->
            CompositionLocalProvider(
                LocalNavSharedTransitionScope provides this@SharedTransitionScope,
                LocalAnimatedVisibilityScope provides this
            ) {
                when (val componentHolder = child.instance) {
                    is RootLibraryFlowComponent.Child.LibraryRoot -> {
                        LibraryScreen(component = componentHolder.component)
                    }

                    is RootLibraryFlowComponent.Child.PhotoViewer -> {
                        PublishedFullscreenPhotoViewer(component = componentHolder.component)
                    }
                }
            }
        }
    }
}