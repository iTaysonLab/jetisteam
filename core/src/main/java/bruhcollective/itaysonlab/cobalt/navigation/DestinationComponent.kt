package bruhcollective.itaysonlab.cobalt.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner

/**
 * A truly unified navigation stack component.
 *
 * This component represents an abstract "destination" that hosts a stack for respecting bottom navigation entry.
 * Also, this component's stack child can be one of ANY screens. This is made to reduce wrappers and to flatten hierarchy.
 *
 * So, in general, this hosts ALL Cobalt screens, except for root (the bottom bar), authorization and fullscreen components (like the photo viewer).
 *
 * Before:
 * NewsFlowComponent(NewsfeedComponent, ArticleComponent, GameFlowComponent(GamePageComponent, UserFlowComponent(UserPageComponent, ...))
 *
 * Now:
 * DestinationComponent(NewsfeedComponent, ArticleComponent, GamePageComponent, UserPageComponent, ...)
 *
 * The downside is probably the HUGE amount of lines in when (..) closure, but it needs to be done four times in total (Config, Child, Child creation, Compose render)
 */
interface DestinationComponent: BackHandlerOwner {
    /**
     * Defines a stack of possible destination children.
     */
    val stack: Value<ChildStack<*, DestinationChild>>

    /**
     * Handles back button presses.
     */
    fun onBackPressed()

    /**
     * Handles back stack reset events (like the tap of a selected navigation bar item).
     *
     * Returns true if stack was reset and "scroll to top" must not be called.
     */
    fun onResetStackPressed(): Boolean
}