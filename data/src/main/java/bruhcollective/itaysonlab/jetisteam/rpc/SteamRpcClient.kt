package bruhcollective.itaysonlab.jetisteam.rpc

import com.squareup.wire.WireRpc
import java.lang.reflect.Proxy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SteamRpcClient @Inject constructor(
    private val processor: SteamRpcProcessor
) {
    inline fun <reified T> create() = create(T::class.java)

    @Suppress("UNCHECKED_CAST")
    fun <T> create(serviceRef: Class<T>): T {
        validateService(serviceRef)
        return Proxy.newProxyInstance(
            serviceRef.classLoader,
            arrayOf(serviceRef),
            SuspendInvocationHandler { proxy, method, args ->
                return@SuspendInvocationHandler processor.execute(
                    method.annotations.find { it is WireRpc } as? WireRpc ?: return@SuspendInvocationHandler method.invoke(proxy, args),
                    args
                )
            }) as T
    }

    private fun <T> validateService(service: Class<T>) {
        require(service.isInterface) { "Service must be an interface" }
    }
}