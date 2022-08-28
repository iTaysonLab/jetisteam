package bruhcollective.itaysonlab.jetisteam.rpc

import com.google.protobuf.RpcCallback
import com.google.protobuf.RpcController

class SteamRpcController(
    val post: Boolean = false
): RpcController {
    private var failed = false
    private var failReason = ""

    override fun failed() = failed
    override fun errorText() = failReason

    override fun setFailed(reason: String?) {
        failed = true
        failReason = reason ?: ""
    }

    override fun reset() {
        failed = false
        failReason = ""
    }

    override fun isCanceled() = error("Cancelling requests is not yet supported!")
    override fun startCancel() = error("Cancelling requests is not yet supported!")
    override fun notifyOnCancel(callback: RpcCallback<Any>?) = error("Cancelling requests is not yet supported!")
}