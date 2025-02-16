package bruhcollective.itaysonlab.cobalt.news

import bruhcollective.itaysonlab.cobalt.news.models.NewsfeedType

interface PickNewsfeedComponent {
    val selectedFeed: NewsfeedType

    fun selectFeed(type: NewsfeedType)
    fun dismiss()
}