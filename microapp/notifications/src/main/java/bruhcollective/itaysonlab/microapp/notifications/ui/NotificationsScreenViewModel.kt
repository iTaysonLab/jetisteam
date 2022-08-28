package bruhcollective.itaysonlab.microapp.notifications.ui

import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.GetNotifications
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class NotificationsScreenViewModel @Inject constructor(
    private val getNotifications: GetNotifications
): PageViewModel<GetNotifications.NotificationsPage>() {
    init { reload() }
    override suspend fun load() = getNotifications()
}