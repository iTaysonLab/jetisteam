package bruhcollective.itaysonlab.microapp.guard.core

import bruhcollective.itaysonlab.jetisteam.controllers.UuidController
import bruhcollective.itaysonlab.jetisteam.models.MobileConfGetList
import bruhcollective.itaysonlab.jetisteam.models.MobileConfirmationItem
import bruhcollective.itaysonlab.jetisteam.service.MobileConfService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuardConfirmationController @Inject constructor(
    private val api: MobileConfService,
    private val uuid: UuidController,
) {
    suspend fun getConfirmations(instance: GuardInstance): MobileConfGetList {
        return instance.confirmationTicket("list").let { sigStamp ->
            api.getConfirmations(
                steamId = instance.steamId.steamId,
                timestamp = sigStamp.generationTime,
                signature = sigStamp.b64EncodedSignature,
                platform = uuid.uuid
            )
        }
    }

    suspend fun setConfirmationStatus(instance: GuardInstance, item: MobileConfirmationItem, allow: Boolean): Boolean {
        val tag = if (allow) {
            "accept"
        } else {
            "reject"
        }

        val operation = if (allow) {
            "allow"
        } else {
            "cancel"
        }

        return instance.confirmationTicket(tag).let { sigStamp ->
            api.runOperation(
                steamId = instance.steamId.steamId,
                timestamp = sigStamp.generationTime,
                signature = sigStamp.b64EncodedSignature,
                platform = uuid.uuid,
                cid = item.id,
                ck = item.nonce,
                tag = tag,
                operation = operation
            ).success
        }
    }

    suspend fun generateDetailPageUrl(
        instance: GuardInstance,
        item: MobileConfirmationItem
    ): String {
        val sigStamp = instance.confirmationTicket("detail")
        val b64 = withContext(Dispatchers.IO) { URLEncoder.encode(sigStamp.b64EncodedSignature, "UTF-8") }
        return "https://steamcommunity.com/mobileconf/detailspage/${item.id}?p=${uuid.uuid}&a=${instance.steamId.steamId}&k=$b64&t=${sigStamp.generationTime}&m=react&tag=detail"
    }
}