package bruhcollective.itaysonlab.jetisteam.rpc

import android.os.Looper
import android.util.Base64
import com.google.protobuf.BlockingRpcChannel
import com.google.protobuf.Descriptors
import com.google.protobuf.Message
import com.google.protobuf.RpcController
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SteamRpcChannel @Inject constructor(
    @Named("steamOkhttp") private val okHttpClient: OkHttpClient
) : BlockingRpcChannel {
    companion object {
        private const val FIELD_NAME = "input_protobuf_encoded"
        const val API_URL = "https://api.steampowered.com"
    }

    override fun callBlockingMethod(
        method: Descriptors.MethodDescriptor,
        controller: RpcController,
        request: Message,
        responsePrototype: Message
    ): Message {
        verifyMainThread()

        val urlPath = "$API_URL/I${method.service.fullName}Service/${method.name}/v1"
        val isPostRequest = (controller as? SteamRpcController)?.post ?: false

        val requestCall = if (isPostRequest) {
            Request.Builder()
                .url(urlPath.toHttpUrl())
                .post(
                    MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(
                        FIELD_NAME,
                        Base64.encodeToString(request.toByteArray(), Base64.NO_PADDING or Base64.NO_WRAP)
                    ).build()
                )
                .build()
        } else {
            Request.Builder()
                .url(
                    urlPath.toHttpUrl()
                        .newBuilder()
                        .addQueryParameter(
                            FIELD_NAME,
                            Base64.encodeToString(request.toByteArray(), Base64.URL_SAFE)
                        )
                        .build()
                )
                .build()
        }

        return okHttpClient.newCall(requestCall).execute().use { r ->
            responsePrototype
                .newBuilderForType()
                .mergeFrom(r.body!!.byteStream())
                .build()
        }
    }

    private fun verifyMainThread() =
        require(Looper.getMainLooper() != Looper.myLooper()) { "RPC methods should be called on non-main thread!" }
}