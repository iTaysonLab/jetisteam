package bruhcollective.itaysonlab.microapp.notifications.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.notifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NotificationsScreenViewModel @Inject constructor(
    private val hostSteamClient: HostSteamClient
): ViewModel() {
    val feed get() = hostSteamClient.client.notifications.notifications
    val confirmationCount get() = hostSteamClient.client.notifications.confirmationCount

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            load()
        }
    }

    suspend fun load() {
        hostSteamClient.client.notifications.requestNotifications(false)
    }

    fun swipeRefreshReload() {
        isRefreshing = true
        viewModelScope.launch {
            load()
            isRefreshing = false
        }
    }
}