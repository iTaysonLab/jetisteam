package bruhcollective.itaysonlab.jetisteam.controllers

import java.util.UUID
import javax.inject.Inject

class UuidController @Inject constructor(
    configService: ConfigService
) {
    val uuid by configService.LazyStringCfg("app.uuid") { "android:${UUID.randomUUID()}" }
}