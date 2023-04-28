package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.ksteam.handlers.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.g0dkar.qrcode.QRCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrCodeBottomsheetViewModel @Inject constructor(
    private val account: Account
): ViewModel() {
    val authFlow get() = account.clientAuthState

    var qrCodeState by mutableStateOf<QrCodeState>(QrCodeState.Loading)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            qrCodeState = account.getSignInQrCode()!!.let {
                QrCodeState.Ready(
                    QRCode(it.data)
                )
            }
        }
    }

    sealed class QrCodeState {
        object Loading : QrCodeState()
        object Error : QrCodeState()

        class Ready(
            val graphicsToRender: QRCode
        ): QrCodeState()
    }

    override fun onCleared() {
        super.onCleared()
        account.cancelPolling()
    }
}