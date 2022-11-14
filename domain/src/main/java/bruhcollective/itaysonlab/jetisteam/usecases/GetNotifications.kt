package bruhcollective.itaysonlab.jetisteam.usecases

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.controllers.UserService
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.models.steamIdFromAccountId
import bruhcollective.itaysonlab.jetisteam.repository.EconRepository
import bruhcollective.itaysonlab.jetisteam.repository.FriendsRepository
import bruhcollective.itaysonlab.jetisteam.repository.NotificationsRepository
import bruhcollective.itaysonlab.jetisteam.repository.StoreRepository
import bruhcollective.itaysonlab.jetisteam.service.MiniprofileService
import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import steam.common.StoreBrowseItemDataRequest
import steam.common.StoreItemID
import steam.steamnotification.SteamNotificationType
import javax.inject.Inject

class GetNotifications @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
    private val econRepository: EconRepository,
    private val friendsRepository: FriendsRepository,
    private val storeRepository: StoreRepository,
    private val steamSessionController: SteamSessionController,
    private val miniprofileService: MiniprofileService
) {
    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        val apiNotifications = notificationsRepository.getNotifications()

        val apiNotificationList = apiNotifications.notifications
            .groupBy { it.timestamp }
            .values
            .map { it.first() to (it.size - 1) }
            .sortedByDescending { it.first.timestamp }

        val shouldRequestFriends =
            apiNotificationList.filter { it.first.notification_type == SteamNotificationType.FriendInvite }
        val shouldRequestGames =
            apiNotificationList.filter { it.first.notification_type == SteamNotificationType.Wishlist }

        val games = (if (shouldRequestGames.isNotEmpty()) storeRepository.getItems(
            shouldRequestGames.map {
                StoreItemID(appid = JSONObject(it.first.body_data.orEmpty()).getInt("appid"))
            }, StoreBrowseItemDataRequest(include_tag_count = 0, include_assets = true)
        ).store_items else emptyList()).associateBy { it.appid }

        val friends =
            (if (shouldRequestFriends.isNotEmpty()) friendsRepository.getFriendsList().friendslist?.friends else emptyList())?.filter {
                it.efriendrelationship == 3
            }?.map {
                SteamID(it.ulfriendid ?: 0).accountId
            } ?: emptyList()

        NotificationsPage(
            confirmationsCount = apiNotifications.confirmation_count ?: 0,
            notifications = apiNotificationList.mapNotNull { pair ->
                val notification = pair.first
                val extra = pair.second
                val haveExtra = extra > 0

                when (notification.notification_type) {
                    SteamNotificationType.Wishlist -> {
                        val game = games[JSONObject(notification.body_data.orEmpty()).getInt("appid")]!!

                        Notification(
                            timestamp = notification.timestamp ?: 0,
                            title = if (haveExtra) "${game.name} + $extra" else game.name.orEmpty(),
                            description = "is on sale for ${game.best_purchase_option?.formatted_final_price.orEmpty()}",
                            icon = CdnUrlUtil.buildCommunityUrl("images/apps/${game.appid}/${game.assets?.community_icon}.jpg"),
                            type = notification.notification_type!!
                        )
                    }

                    SteamNotificationType.FriendInvite -> {
                        val accountId =
                            JSONObject(notification.body_data.orEmpty()).getInt("requestor_id").toLong()
                        val miniProfile = miniprofileService.getMiniprofile(accountId)
                        val inFriends = friends.contains(accountId)

                        Notification(
                            timestamp = notification.timestamp ?: 0,
                            title = if (haveExtra) "${miniProfile.personaName} + $extra" else miniProfile.personaName,
                            description = if (inFriends) "You are now friends" else "Awaiting response",
                            icon = miniProfile.avatarUrl,
                            type = notification.notification_type!!
                        )
                    }

                    /*SteamNotificationType.Item -> {
                        val itemId = JSONObject(notification.bodyData)

                        val item = econRepository.getInventoryItemsWithDescriptions(
                            steamid = steamSessionController.steamId().steamId,
                            appid = itemId.getInt("app_id"),
                            contextid = itemId.getLong("context_id"),
                            getDescriptions = true,
                            assetids = listOf(itemId.getLong("asset_id"))
                        ).descriptionsList.first()

                        Notification(
                            timestamp = notification.timestamp,
                            title = "New items",
                            description = if (haveExtra) "${item.name} + $extra" else item.name,
                            icon = CdnUrlUtil.buildEconomy(item.iconUrl),
                            type = notification.notificationType
                        )
                    }*/

                    else -> null /*Notification(
                        timestamp = notification.timestamp,
                        title = "Unknown notification",
                        description = "Not yet implemented!",
                        icon = "",
                        type = notification.notificationType
                    )*/
                }
            }
        )
    }

    class NotificationsPage(
        val confirmationsCount: Int,
        val notifications: List<Notification>
    )

    class Notification(
        val timestamp: Int,
        val icon: String,
        val title: String,
        val description: String,
        val type: SteamNotificationType
    )
}