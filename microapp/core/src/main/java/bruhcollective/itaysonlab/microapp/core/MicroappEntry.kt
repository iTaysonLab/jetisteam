package bruhcollective.itaysonlab.microapp.core

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.composable

typealias Destinations = Map<Class<out MicroappEntry>, @JvmSuppressWildcards MicroappEntry>

interface MicroappEntry {
    val microappRoute: String

    val arguments: List<NamedNavArgument>
        get() = emptyList()

    val deepLinks: List<NavDeepLink>
        get() = emptyList()

    val fullscreenRoutes: List<String>
        get() = emptyList()
}

interface ComposableMicroappEntry: MicroappEntry {
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
    fun NavGraphBuilder.navigation(navController: NavHostController, destinations: Destinations)
}

inline fun <reified T : MicroappEntry> Destinations.find(): T = findOrNull() ?: error("Destination '${T::class.java}' is not defined.")
inline fun <reified T : MicroappEntry> Destinations.findOrNull(): T? = this[T::class.java] as? T