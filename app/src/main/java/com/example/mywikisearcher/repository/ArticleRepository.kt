package com.example.mywikisearcher.repository

import com.example.mywikisearcher.model.ArticleEntity
import com.example.mywikisearcher.model.QueryResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val service: ApiService,
    private val dao: BookmarkDao
) {
    private val _searchResults = MutableStateFlow<List<QueryResponse.Query.Page>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    val bookmarks = dao.getAllBookmarks()

    suspend fun searchArticles(query: String, maxResults: Int, startFromIndex: Int) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        // Fetch a list of articles from Wikipedia!
        val response = service.prefixSearch(query, maxResults, startFromIndex)

        // Filter out articles that don't have a thumbnail!
        val articlesWithThumbnails = response.query?.pages?.filter {
            it.thumbnail != null
        } ?: emptyList()

        _searchResults.value = if (startFromIndex == 0) {
            articlesWithThumbnails
        } else {
            _searchResults.value + articlesWithThumbnails
        }
    }

    suspend fun removeBookmark(article: ArticleEntity) {
        dao.deleteBookmark(article)
    }

    suspend fun addBookmark(article: ArticleEntity) {
        dao.insertOrReplaceBookmark(article)
    }
}
