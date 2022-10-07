package bruhcollective.itaysonlab.microapp.guard.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bruhcollective.itaysonlab.jetisteam.proto.GuardData

class GuardInstance(
    private val steamId: Long,
    private var data: GuardData?
) {
    private val sharedSecret get() = data?.shared_secret

    var state by mutableStateOf(data?.let { GuardState.Ready } ?: GuardState.Empty)
        private set

    private fun generateCode() {

    }

    enum class GuardState {
        Empty, // No guard added with this app
        Setup, // Setup, waiting for a number
        Ready // Ready for generating codes
    }
}