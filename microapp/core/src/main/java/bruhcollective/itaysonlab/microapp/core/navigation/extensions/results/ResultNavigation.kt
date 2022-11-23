package bruhcollective.itaysonlab.microapp.core.navigation.extensions.results

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import kotlinx.coroutines.CoroutineScope

@Stable
interface NavigationResult: Parcelable

fun NavController.setResultToPreviousEntry(result: NavigationResult, popBackStack: Boolean = true) {
    previousBackStackEntry?.savedStateHandle?.set(CommonArguments.Result.name, result)

    if (popBackStack) {
        popBackStack()
    }
}

@Composable
fun InstallResultHandler(backStack: NavBackStackEntry, block: suspend CoroutineScope.(NavigationResult) -> Unit) {
    val resultState = backStack.savedStateHandle.getStateFlow<NavigationResult?>(CommonArguments.Result.name, null).collectAsState()

    LaunchedEffect(resultState) {
        block(resultState.value ?: return@LaunchedEffect)
        backStack.savedStateHandle[CommonArguments.Result.name] = null
    }
}

@Composable
inline fun <reified T: NavigationResult> InstallTypedResultHandler(backStack: NavBackStackEntry, crossinline block: suspend CoroutineScope.(T) -> Unit) {
    InstallResultHandler(backStack) { result ->
        block(
            (result as? T) ?: error("Expected ${T::class.java.simpleName}, got ${result.javaClass.simpleName}")
        )
    }
}