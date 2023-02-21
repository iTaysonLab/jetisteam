package bruhcollective.itaysonlab.microapp.profile.ui.screens.friends

/*import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.mappers.FriendProfile
import bruhcollective.itaysonlab.jetisteam.mappers.FriendStatus
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.profile.GetProfileFriends
import bruhcollective.itaysonlab.microapp.profile.ext.FriendGroups
import bruhcollective.itaysonlab.microapp.profile.ext.toSortingInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FriendsScreenViewModel @Inject constructor(
    private val getFriends: GetProfileFriends,
) : PageViewModel<Map<FriendGroups, List<FriendProfile>>>() {

    init { reload() }

    override suspend fun load() = with(getFriends()) {
        val playing = filter { it.playingGame != null }.sortedByDescending { it.name }
        val offline = filter { it.status is FriendStatus.Offline }
                        .sortedByDescending { (it.status as FriendStatus.Offline).lastLogoff }
        mapOf(
            FriendGroups.PLAYING to playing,
            FriendGroups.ONLINE to filter { it !in playing && it !in offline }
                                        .sortedWith(
                                            compareBy<FriendProfile>
                                            { it.status.toSortingInt() }
                                                .thenByDescending { it.name },
                                        ),
            FriendGroups.OFFLINE to offline,
        )
    }

    private val _swipeRefreshLoading = MutableStateFlow(false)
    val swipeRefreshLoading = _swipeRefreshLoading.asStateFlow()

    fun swipeRefreshReload() {
        _swipeRefreshLoading.value = true
        viewModelScope.launch {
            setState(load())
            _swipeRefreshLoading.value = false
        }
    }
}*/