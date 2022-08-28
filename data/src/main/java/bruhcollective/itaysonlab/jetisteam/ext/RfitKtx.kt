package bruhcollective.itaysonlab.jetisteam.ext

import okhttp3.OkHttpClient
import okhttp3.Request

internal fun OkHttpClient.Builder.interceptRequest(scope: Request.Builder.(Request) -> Unit) = addInterceptor { chain ->
    val request = chain.request()
    chain.proceed(request.newBuilder().apply { scope(this, request) }.build())
}