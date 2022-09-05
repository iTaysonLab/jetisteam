package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import steam.friendslist.CFriendsList_GetFriendsList_Request
import steam.friendslist.FriendsList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    steamRpcClient: SteamRpcClient,
) {
    private val stub = steamRpcClient.create<FriendsList>()

    suspend fun getFriendsList() = stub.GetFriendsList(CFriendsList_GetFriendsList_Request())
}