package bruhcollective.itaysonlab.microapp.guard.core

import bruhcollective.itaysonlab.jetisteam.controllers.UuidController
import bruhcollective.itaysonlab.jetisteam.models.MobileConfGetList
import bruhcollective.itaysonlab.jetisteam.service.MobileConfService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuardConfirmationController @Inject constructor(
    private val api: MobileConfService,
    private val uuid: UuidController,
    private val guardClockNormalizer: GuardClockNormalizer
) {
    suspend fun getConfirmations(instance: GuardInstance): MobileConfGetList {
        val sigStamp = instance.confirmationTicket(guardClockNormalizer, "list")

        return api.getConfirmations(
            steamId = instance.steamId.steamId,
            timestamp = sigStamp.generationTime,
            signature = sigStamp.b64EncodedSignature,
            platform = uuid.uuid
        )
    }
}