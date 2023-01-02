package bruhcollective.itaysonlab.microapp.library.ui.bottomsheet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.ext.getString
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.ByteString.Companion.decodeBase64
import steam.clientcomm.CClientComm_GetAllClientLogonInfo_Response_Session
import javax.inject.Inject

@HiltViewModel
internal class PickRemoteDeviceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val steamId = savedStateHandle.getSteamId()

    val machines = savedStateHandle.getString(LibraryMicroapp.Arguments.MachineList.name).split("-").mapNotNull {
        CClientComm_GetAllClientLogonInfo_Response_Session.Companion.ADAPTER.decode(it.decodeBase64() ?: return@mapNotNull null)
    }
}