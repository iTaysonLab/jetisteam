package bruhcollective.itaysonlab.jetisteam.controllers

import bruhcollective.itaysonlab.jetisteam.proto.GuardData
import bruhcollective.itaysonlab.jetisteam.proto.SessionData
import bruhcollective.itaysonlab.ksteam.proto.GuardConfiguration
import okhttp3.internal.EMPTY_BYTE_ARRAY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsLegacyController @Inject constructor(
    private val cfgService: ConfigService,
) {
    var authSession by cfgService.protoCfg(key = "steam.session", adapter = SessionData.ADAPTER)
        private set

    fun getGuard(): GuardConfiguration? {
        authSession ?: return null

        val key = "guard.${authSession?.steam_id}"
        val legacyBytes = cfgService.getBytes(ConfigService.Instance.Main, key = key, default = EMPTY_BYTE_ARRAY)

        return if (legacyBytes.isNotEmpty()) {
            GuardData.ADAPTER.decode(legacyBytes).let { data ->
                GuardConfiguration(
                    shared_secret = data.shared_secret,
                    serial_number = data.serial_number,
                    revocation_code = data.revocation_code,
                    uri = data.uri,
                    server_time = data.server_time,
                    account_name = data.account_name,
                    token_gid = data.token_gid,
                    identity_secret = data.identity_secret,
                    secret_1 = data.secret_1,
                    steam_id = authSession!!.steam_id
                ).also {
                    cfgService.deleteKey(ConfigService.Instance.Main, key = key)
                }
            }
        } else {
            null
        }
    }
}