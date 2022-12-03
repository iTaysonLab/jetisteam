package bruhcollective.itaysonlab.microapp.guard.ui.confirmations

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.models.MobileConfGetList
import bruhcollective.itaysonlab.jetisteam.models.MobileConfirmationItem
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.guard.core.GuardConfirmationController
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import bruhcollective.itaysonlab.microapp.guard.ui.confirmation_detail.ConfirmationDetailViewModel
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.ByteString.Companion.encodeUtf8
import javax.inject.Inject

@HiltViewModel
internal class ConfirmationsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    guardController: GuardController,
    private val guardConfirmationController: GuardConfirmationController,
    private val moshi: Moshi
) : PageViewModel<MobileConfGetList>() {
    val steamId = savedStateHandle.getSteamId()
    private val guardInstance = guardController.getInstance(steamId) ?: error("Confirmations should not be visible for steamId $steamId")

    init {
        reload()
    }

    override suspend fun load(): MobileConfGetList {
        return guardConfirmationController.getConfirmations(guardInstance)
    }

    fun wrapConfirmation(item: MobileConfirmationItem): String {
        return moshi.adapter(MobileConfirmationItem::class.java).toJson(item).encodeUtf8().base64Url()
    }

    fun consumeEvent(event: ConfirmationDetailViewModel.ConfirmationDetailResult) {
        val previousData = data ?: return

        setState(
            previousData.copy(
                conf = previousData.conf?.filterNot { it.id == event.id }.orEmpty()
            )
        )
    }
}