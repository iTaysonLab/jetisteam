package bruhcollective.itaysonlab.cobalt.news.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class NewsfeedPagingSource: PagingSource<NewsfeedPagingKey, NewsfeedPagingItem>() {
    override fun getRefreshKey(state: PagingState<NewsfeedPagingKey, NewsfeedPagingItem>): NewsfeedPagingKey? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<NewsfeedPagingKey>): LoadResult<NewsfeedPagingKey, NewsfeedPagingItem> {
        TODO("Not yet implemented")
    }
}