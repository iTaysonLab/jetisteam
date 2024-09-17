package bruhcollective.itaysonlab.cobalt.compose

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalNavSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@ReadOnlyComposable
fun inSharedTransitionScope(): Boolean = LocalNavSharedTransitionScope.current != null

@Composable
@ReadOnlyComposable
fun inSharedTransitionScopeWithAnimatedVisibility(): Boolean = inSharedTransitionScope() && LocalAnimatedVisibilityScope.current != null

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@ReadOnlyComposable
fun nonNullableNavSharedTransitionScope(): SharedTransitionScope = LocalNavSharedTransitionScope.current ?: error("SharedTransitionScope was not defined in this composition")

@Composable
@ReadOnlyComposable
fun nonNullableAnimatedVisibilityScope(): AnimatedVisibilityScope = LocalAnimatedVisibilityScope.current ?: error("AnimatedVisibilityScope was not defined in this composition")

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun rememberSharedContentState(
    sharedTransitionScope: SharedTransitionScope = LocalNavSharedTransitionScope.current ?: error("SharedTransitionScope was not defined in this composition"),
    key: Any
) = with(sharedTransitionScope) {
    rememberSharedContentState(key)
}