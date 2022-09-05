package bruhcollective.itaysonlab.jetisteam.controllers

import bruhcollective.itaysonlab.jetisteam.proto.SessionData
import bruhcollective.itaysonlab.jetisteam.repository.NeutralAuthRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SteamAuthInterceptor @Inject constructor(
    private val steamSessionController: SteamSessionController,
    private val neutralAuthRepository: NeutralAuthRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val useCookies = request.url.host != "api.steampowered.com"
        val authSession = steamSessionController.authSession

        authSession ?: return chain.proceed(request) // no auth data

        val newRequestBuilder = { request.newBuilder().addAccountData(steamSessionController.authSession!!, request, useCookies).build() }
        val response = chain.proceed(newRequestBuilder())

        return if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            // recreate token from refresh
            response.close()
            neutralAuthRepository.refreshSession()
            chain.proceed(newRequestBuilder())
        } else {
            response
        }
    }

    private fun Request.Builder.addAccountData(authSession: SessionData, orig: Request, useCookies: Boolean): Request.Builder {
        if (useCookies) {
            header(
                "Cookie", "mobileClient=android; mobileClientVersion=777777 3.0.0; steamLoginSecure=${steamSessionController.buildSteamLoginSecureCookie()}"
            )
        } else {
            url(
                orig.url.newBuilder().addQueryParameter("access_token", authSession.access_token).build()
            )
        }

        return this
    }
}