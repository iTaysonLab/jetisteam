package bruhcollective.itaysonlab.microapp.notifications.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.GetNotifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NotificationsScreenViewModel @Inject constructor(
    private val getNotifications: GetNotifications
): PageViewModel<GetNotifications.NotificationsPage>() {
    var isRefreshing by mutableStateOf(false)
        private set

    init { reload() }
    override suspend fun load() = getNotifications()

    fun swipeRefreshReload() {
        isRefreshing = true
        viewModelScope.launch {
            setState(load())
            isRefreshing = false
        }
    }
}