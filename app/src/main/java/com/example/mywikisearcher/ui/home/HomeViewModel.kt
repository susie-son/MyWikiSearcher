package com.example.mywikisearcher.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywikisearcher.model.Article
import com.example.mywikisearcher.model.QueryResponse
import com.example.mywikisearcher.repository.BookmarkDao
import com.example.mywikisearcher.repository.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val service: Service,
    private val bookmarkDao: BookmarkDao
): ViewModel() {

    private val searchResultList = MutableStateFlow<List<QueryResponse.Query.Page>>(emptyList())
    private val bookmarksList = bookmarkDao.getAllBookmarks()
    private val _selectedTab = MutableStateFlow(HomeTab.Search)
    val selectedTab: StateFlow<HomeTab> = _selectedTab.asStateFlow()

    val searchText = mutableStateOf("")

    val articleList: Flow<List<Article>> = combine(searchResultList, bookmarksList, selectedTab) { search, bookmarks, tab ->
        when (tab) {
            HomeTab.Search -> search.map { item ->
                Article(
                    pageId = item.pageId,
                    title = item.title,
                    description = item.description,
                    thumbnail = item.thumbnail?.source,
                    isBookmarked = bookmarks.any { it.pageId == item.pageId }
                )
            }
            HomeTab.Bookmarks -> bookmarks.map { item ->
                Article(
                    pageId = item.pageId,
                    title = item.title,
                    description = item.description,
                    thumbnail = item.thumbnail?.source,
                    isBookmarked = bookmarks.any { it.pageId == item.pageId }
                )
            }
        }
    }

    fun changeSearchText(text: String) {
        searchText.value = text
        searchWiki(text)
    }

    fun selectTab(tab: HomeTab) {
        _selectedTab.value = tab
    }

    private fun searchWiki(text: String, startFromIndex: Int = 0) {
        if (text.isEmpty()) return
        viewModelScope.launch {
            // Fetch a list of articles from Wikipedia!
            val response = service.prefixSearch(text.toString(), 20, startFromIndex)

            // Filter out articles that don't have a thumbnail!
            val finalList = mutableListOf<QueryResponse.Query.Page>()
            response.query?.pages!!.forEach {
                if (it.thumbnail != null) {
                    finalList.add(it)
                }
            }

            if (startFromIndex == 0) {
                searchResultList.value = emptyList()
            }

            searchResultList.value = finalList
        }
    }

    fun handleBookmark(article: Article) {
        val queryPage = QueryResponse.Query.Page(
            pageId = article.pageId,
            title = article.title,
            description = article.description,
            thumbnail = article.thumbnail?.let {
                QueryResponse.Query.Page.Thumbnail(
                    source = it
                )
            }
        )
        viewModelScope.launch {
            if (article.isBookmarked) {
                bookmarkDao.deleteBookmark(queryPage)
            } else {
                bookmarkDao.insertBookmark(queryPage)
            }
        }
    }
}
