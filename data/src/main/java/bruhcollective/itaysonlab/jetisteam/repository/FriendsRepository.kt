package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcChannel
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.friendslist.CFriendsList_GetFriendsList_Request
import steam.friendslist.FriendsList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    steamRpcChannel: SteamRpcChannel
) {
    private val stub = FriendsList.newBlockingStub(steamRpcChannel)

    suspend fun getFriendsList() = withContext(Dispatchers.IO) {
        stub.getFriendsList(
            SteamRpcController(), CFriendsList_GetFriendsList_Request.getDefaultInstance()
        )
    }
}