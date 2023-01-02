package bruhcollective.itaysonlab.jetisteam.usecases

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.controllers.UserService
import bruhcollective.itaysonlab.jetisteam.data.R
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.models.steamIdFromAccountId
import bruhcollective.itaysonlab.jetisteam.repository.EconRepository
import bruhcollective.itaysonlab.jetisteam.repository.FriendsRepository
import bruhcollective.itaysonlab.jetisteam.repository.NotificationsRepository
import bruhcollective.itaysonlab.jetisteam.repository.StoreRepository
import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import bruhcollective.itaysonlab.jetisteam.util.FormattedResourceString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import steam.common.StoreBrowseItemDataRequest
import steam.common.StoreItemID
import steam.steamnotification.SteamNotificationData
import steam.steamnotification.SteamNotificationType
import javax.inject.Inject

class GetNotifications @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
    private val econRepository: EconRepository,
    private val friendsRepository: FriendsRepository,
    private val storeRepository: StoreRepository,
    private val steamSessionController: SteamSessionController,
    private val userService: UserService
) {
    private fun SteamNotificationData.getUserSteamId(): SteamID? {
        return when (notification_type) {
            SteamNotificationType.FriendInvite -> steamIdFromAccountId(JSONObject(body_data.orEmpty()).optLong("requestor_id"))
            SteamNotificationType.Gift -> steamIdFromAccountId(JSONObject(body_data.orEmpty()).optLong("gifter_account"))
            else -> null
        }
    }

    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        val apiNotifications = notificationsRepository.getNotifications()

        val apiNotificationList = apiNotifications.notifications
            .partition { it.notification_type == SteamNotificationType.Item }
            .let { itemsAndOthers ->
                val indexOfFirstItemNotification =
                    apiNotifications.notifications.sortedByDescending { it.timestamp ?: 0 }
                        .indexOfFirst { it.notification_type == SteamNotificationType.Item }

                val itemWithExtras = if (itemsAndOthers.first.isNotEmpty()) {
                    itemsAndOthers.first.maxBy {
                        it.timestamp ?: 0
                    } to (itemsAndOthers.first.size - 1)
                } else {
                    null
                }

                return@let itemsAndOthers.second
                    .map { it to 0 }
                    .toMutableList()
                    .also {
                        if (itemWithExtras != null) {
                            it.add(indexOfFirstItemNotification, itemWithExtras)
                        }
                    }
            }
            .sortedByDescending { it.first.timestamp ?: 0 }

        val shouldRequestFriends =
            apiNotificationList.filter { it.first.notification_type == SteamNotificationType.FriendInvite }

        val shouldRequestGames =
            apiNotificationList.filter { it.first.notification_type == SteamNotificationType.Wishlist }

        val games = (if (shouldRequestGames.isNotEmpty()) storeRepository.getItems(
            shouldRequestGames.map {
                StoreItemID(appid = JSONObject(it.first.body_data.orEmpty()).getInt("appid"))
            }, StoreBrowseItemDataRequest(include_tag_count = 0, include_assets = true)
        ).store_items else emptyList()).filter { it.appid != null }.associateBy { it.appid!! }

        val friends =
            (if (shouldRequestFriends.isNotEmpty()) friendsRepository.getFriendsList().friendslist?.friends else emptyList())?.filter {
                it.efriendrelationship == 3
            }?.map {
                SteamID(it.ulfriendid ?: 0)
            } ?: emptyList()

        val profiles =
            apiNotificationList.mapNotNull {
                it.first.getUserSteamId()
            }.let { ids ->
                userService.resolveUsers(ids.map(SteamID::steamId))
            }

        NotificationsPage(
            confirmationsCount = apiNotifications.confirmation_count ?: 0,
            notifications = apiNotificationList.mapNotNull { pair ->
                val notification = pair.first
                val extra = pair.second
                val haveExtra = extra > 0

                when (notification.notification_type) {
                    SteamNotificationType.Wishlist -> {
                        val appId = JSONObject(notification.body_data.orEmpty()).optInt("appid")

                        if (appId != 0) {
                            val game = games[appId] ?: return@mapNotNull null

                            Notification(
                                timestamp = notification.timestamp ?: 0,
                                title = FormattedResourceString.FixedString(game.name.orEmpty()),
                                description = FormattedResourceString.ResourceId(R.string.notifications_type_wishlist_desc, game.best_purchase_option?.formatted_final_price.orEmpty()),
                                icon = CdnUrlUtil.buildCommunityUrl("images/apps/${game.appid}/${game.assets?.community_icon}.jpg"),
                                type = notification.notification_type!!,
                                unread = notification.read?.not() ?: false,
                                destination = game.appid ?: 0
                            )
                        } else {
                            Notification(
                                timestamp = notification.timestamp ?: 0,
                                title = FormattedResourceString.ResourceId(R.string.notifications_type_wishlist_multiple_title),
                                description = FormattedResourceString.ResourceId(R.string.notifications_type_wishlist_multiple_desc),
                                icon = "",
                                type = notification.notification_type!!,
                                unread = notification.read?.not() ?: false,
                                destination = "wishlist"
                            )
                        }
                    }

                    SteamNotificationType.FriendInvite -> {
                        val profile = profiles[notification.getUserSteamId()] ?: return@mapNotNull null
                        val inFriends = friends.contains(profile.steamId)

                        Notification(
                            timestamp = notification.timestamp ?: 0,
                            title = FormattedResourceString.FixedString(profile.personaname),
                            description = FormattedResourceString.ResourceId(if (inFriends) R.string.notifications_type_friend_desc_added else R.string.notifications_type_friend_desc_awaiting),
                            icon = profile.avatarfull.orEmpty(),
                            type = notification.notification_type!!,
                            unread = notification.read?.not() ?: false,
                            destination = profile.steamId.steamId
                        )
                    }

                    SteamNotificationType.Gift -> {
                        val profile = profiles[notification.getUserSteamId()] ?: return@mapNotNull null

                        Notification(
                            timestamp = notification.timestamp ?: 0,
                            title = FormattedResourceString.ResourceId(R.string.notifications_type_gift_title),
                            description = FormattedResourceString.ResourceId(R.string.notifications_type_gift_desc, profile.personaname),
                            icon = profile.avatarfull.orEmpty(),
                            type = notification.notification_type!!,
                            unread = notification.read?.not() ?: false,
                            destination = profile.steamId.steamId
                        )
                    }

                    SteamNotificationType.Item -> {
                        val itemId = JSONObject(notification.body_data.orEmpty())

                        val item = econRepository.getInventoryItemsWithDescriptions(
                            steamid = steamSessionController.steamId().steamId,
                            appid = itemId.getInt("app_id"),
                            contextid = itemId.getLong("context_id"),
                            getDescriptions = true,
                            assetids = listOf(itemId.getLong("asset_id"))
                        ).descriptions.first()

                        Notification(
                            timestamp = notification.timestamp ?: 0,
                            title = FormattedResourceString.ResourceId(if (haveExtra) R.string.notifications_type_item_title_multiple else R.string.notifications_type_item_title),
                            description = FormattedResourceString.FixedString(if (haveExtra) "${item.name} + $extra" else item.name.orEmpty()),
                            icon = CdnUrlUtil.buildEconomy(item.icon_url.orEmpty()),
                            type = notification.notification_type!!,
                            unread = notification.read?.not() ?: false,
                            destination = steamSessionController.steamId().steamId
                        )
                    }

                    SteamNotificationType.Promotion -> {
                        val promoObject = JSONObject(notification.body_data.orEmpty())

                        Notification(
                            timestamp = notification.timestamp ?: 0,
                            title = FormattedResourceString.FixedString(promoObject.optString("title").orEmpty()),
                            description = FormattedResourceString.FixedString(promoObject.optString("body").orEmpty()),
                            icon = promoObject.optString("image").orEmpty(),
                            type = notification.notification_type!!,
                            unread = notification.read?.not() ?: false,
                            destination = promoObject.optString("link").orEmpty()
                        )
                    }

                    else -> Notification(
                        timestamp = notification.timestamp ?: 0,
                        title = FormattedResourceString.FixedString("Unknown notification"),
                        description = FormattedResourceString.FixedString("Rich content not implemented"),
                        icon = "",
                        type = notification.notification_type ?: SteamNotificationType.Item,
                        unread = notification.read?.not() ?: false,
                        destination = ""
                    ).also { println(notification.body_data.orEmpty()) }
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
        val title: FormattedResourceString,
        val description: FormattedResourceString,
        val type: SteamNotificationType,
        val unread: Boolean,
        val destination: Any
    )
}