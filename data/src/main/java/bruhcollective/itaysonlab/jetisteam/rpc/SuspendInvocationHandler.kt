package bruhcollective.itaysonlab.jetisteam.rpc

import kotlinx.coroutines.runBlocking
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import kotlin.coroutines.Continuation

// src: https://discuss.kotlinlang.org/t/how-to-continue-a-suspend-function-in-a-dynamic-proxy-in-the-same-coroutine/11391/5
@Suppress("FunctionName", "NAME_SHADOWING", "UNCHECKED_CAST")
fun SuspendInvocationHandler(block: suspend (proxy: Any, method: Method, args: Array<*>) -> Any?) =
    InvocationHandler { proxy, method, args ->
        val cont = args?.lastOrNull() as? Continuation<*>
        if (cont == null) {
            val args = args.orEmpty()

            runBlocking {
                block(proxy, method, args)
            }
        } else {
            val args = args.dropLast(1).toTypedArray()
            val suspendInvoker = block as (Any, Method, Array<*>?, Continuation<*>) -> Any?

            suspendInvoker(proxy, method, args, cont)
        }
    }
