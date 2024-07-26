package com.example.mywikisearcher.repository

import com.example.mywikisearcher.model.ArticleDatabaseModel
import com.example.mywikisearcher.model.QueryResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val service: ApiService,
    private val dao: BookmarkDao
) {
    private val _searchResultList = MutableStateFlow<List<QueryResponse.Query.Page>>(emptyList())
    val searchResultList = _searchResultList.asStateFlow()

    val bookmarkList = dao.getAllBookmarks()

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

    suspend fun removeBookmark(article: ArticleDatabaseModel) {
        dao.deleteBookmark(article)
    }

    suspend fun addBookmark(article: ArticleDatabaseModel) {
        dao.insertBookmark(article)
    }
}
