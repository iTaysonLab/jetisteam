package bruhcollective.itaysonlab.microapp.core

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

typealias Destinations = Map<Class<out MicroappEntry>, @JvmSuppressWildcards MicroappEntry>

interface MicroappEntry {

}

interface ComposableMicroappEntry: MicroappEntry {
    val microappRoute: String

    val arguments: List<NamedNavArgument>
        get() = emptyList()

    val deepLinks: List<NavDeepLink>
        get() = emptyList()

    @OptIn(ExperimentalAnimationApi::class)
    fun NavGraphBuilder.composable(navController: NavHostController, destinations: Destinations) {
        composable(
            microappRoute, arguments, deepLinks
        ) { backStackEntry ->
            Content(navController, destinations, backStackEntry)
        }
    }

    @Composable
    fun NavGraphBuilder.Content(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    )
}

interface NestedMicroappEntry: MicroappEntry {
    val graphRoute: String
    val startDestination: String

    @OptIn(ExperimentalAnimationApi::class)
    fun NavGraphBuilder.navigation(navController: NavHostController, destinations: Destinations) {
        navigation(
            route = graphRoute,
            startDestination = startDestination,
        ) {
            buildGraph(navController, destinations)
        }
    }

    fun NavGraphBuilder.buildGraph(navController: NavHostController, destinations: Destinations)
}

interface BottomNavigationCapable {
    val bottomNavigationEntry: NavigationEntry
}

interface HasFullscreenRoutes {
    val fullscreenRoutes: List<String>
        get() = emptyList()
}

inline fun <reified T : MicroappEntry> Destinations.find(): T = findOrNull() ?: error("Destination '${T::class.java}' is not defined.")
inline fun <reified T : MicroappEntry> Destinations.findOrNull(): T? = this[T::class.java] as? T

@Stable
class NavigationEntry(
    val route: String,
    @StringRes val name: Int,
    val icon: () -> ImageVector
)