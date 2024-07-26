package com.example.mywikisearcher.repository

import com.example.mywikisearcher.model.Article
import com.example.mywikisearcher.model.QueryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val service: ApiService,
    private val dao: BookmarkDao
) {
    private val _searchResultList = MutableStateFlow<List<QueryResponse.Query.Page>>(emptyList())
    private val _bookmarkList = dao.getAllBookmarks()

    val searchResultList: Flow<List<Article>> = combine(_searchResultList, _bookmarkList) { search, bookmarks ->
        search.map {
            Article(
                pageId = it.pageId,
                title = it.title,
                description = it.description,
                thumbnail = it.thumbnail?.source,
                isBookmarked = bookmarks.any { bookmark -> it.pageId == bookmark.pageId }
            )
        }
    }
    val bookmarkList: Flow<List<Article>> = _bookmarkList.map { bookmarks ->
        bookmarks.map {
            Article(
                pageId = it.pageId,
                title = it.title,
                description = it.description,
                thumbnail = it.thumbnail?.source,
                isBookmarked = true
            )
        }
    }

    suspend fun getSearchList(query: String, maxResults: Int, startFromIndex: Int) {
        if (query.isEmpty()) {
            _searchResultList.value = emptyList()
            return
        }

        // Fetch a list of articles from Wikipedia!
        val response = service.prefixSearch(query, maxResults, startFromIndex)

        // Filter out articles that don't have a thumbnail!
        val thumbnailList = response.query?.pages!!.filter {
            it.thumbnail != null
        }

        val finalList = if (startFromIndex == 0) {
            thumbnailList
        } else {
            _searchResultList.value + thumbnailList
        }

        _searchResultList.value = finalList
    }

    suspend fun removeBookmark(page: QueryResponse.Query.Page) {
        dao.deleteBookmark(page)
    }

    suspend fun addBookmark(page: QueryResponse.Query.Page) {
        dao.insertBookmark(page)
    }
}
