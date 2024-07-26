package com.example.mywikisearcher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywikisearcher.util.Constants.SEARCH_MAX_RESULTS
import com.example.mywikisearcher.model.ArticleState
import com.example.mywikisearcher.model.toArticleEntity
import com.example.mywikisearcher.model.toArticleState
import com.example.mywikisearcher.repository.ArticleRepository
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
    private val repository: ArticleRepository
) : ViewModel() {

    private val searchedArticles = repository.searchResults
    private val bookmarkedArticles = repository.bookmarks

    private val _selectedTab = MutableStateFlow(HomeScreenTab.Search)
    val selectedTab: StateFlow<HomeScreenTab> = _selectedTab.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val articleList: Flow<List<ArticleState>> =
        combine(searchedArticles, bookmarkedArticles, selectedTab) { search, bookmarks, tab ->
            when (tab) {
                HomeScreenTab.Search -> search.map {
                    it.toArticleState(bookmarked = bookmarks.any { bookmark -> bookmark.id == it.pageId })
                }
                HomeScreenTab.Bookmarks -> bookmarks.map { it.toArticleState(bookmarked = true) }
            }
        }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        searchWiki(text)
    }

    fun onTabSelected(tab: HomeScreenTab) {
        _selectedTab.value = tab
    }

    private fun searchWiki(query: String, startFromIndex: Int = 0) {
        viewModelScope.launch {
            repository.searchArticles(query, SEARCH_MAX_RESULTS, startFromIndex)
        }
    }

    fun onBookmarkToggle(article: ArticleState) {
        viewModelScope.launch {
            if (article.bookmarked) {
                repository.removeBookmark(article.toArticleEntity())
            } else {
                repository.addBookmark(article.toArticleEntity())
            }
        }
    }

    fun onLoadMoreResults() {
        if (selectedTab.value != HomeScreenTab.Search) return
        searchWiki(searchText.value, searchedArticles.value.size)
    }
}
