package bruhcollective.itaysonlab.jetisteam.web

import it.skrape.fetcher.NonBlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.Result
import okhttp3.OkHttpClient
import okio.use
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SteamFetcher @Inject constructor(
    @Named("steamOkhttp") private val okHttpClient: OkHttpClient
): NonBlockingFetcher<Request> {
    override val requestBuilder = Request()

    override suspend fun fetch(request: Request): Result {
        return okHttpClient.newCall(
            okhttp3.Request.Builder()
                .url(request.url)
                .build()
        ).execute().use { response ->
            val type = response.body?.contentType()?.type
            val document = response.body?.string()

            Result(
                responseBody = document ?: "",
                responseStatus = Result.Status(response.code, ""),
                contentType = type,
                headers = request.headers,
                baseUri = request.url,
                cookies = emptyList()
            )
        }
    }
}