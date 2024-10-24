package bruhcollective.itaysonlab.cobalt.core.decompose

import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

@Deprecated(message = "Use Essenty built-in CoroutineScope", replaceWith = ReplaceWith(
    expression = "coroutineScope(context)",
    imports = ["com.arkivanov.essenty.lifecycle.coroutines.coroutineScope"]
))
fun LifecycleOwner.componentCoroutineScope(
    context: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate
): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}