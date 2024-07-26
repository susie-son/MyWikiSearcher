package com.example.mywikisearcher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywikisearcher.model.ArticleDatabaseModel
import com.example.mywikisearcher.model.ArticleDisplayModel
import com.example.mywikisearcher.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RESULTS_PAGE_SIZE = 20

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ArticleRepository
): ViewModel() {

    private val searchResultList = repository.searchResultList
    private val bookmarksList = repository.bookmarkList

    private val _selectedTab = MutableStateFlow(HomeTab.Search)
    val selectedTab: StateFlow<HomeTab> = _selectedTab.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val articleList: Flow<List<ArticleDisplayModel>> = combine(searchResultList, bookmarksList, selectedTab) { search, bookmarks, tab ->
        when (tab) {
            HomeTab.Search -> search.map {
                ArticleDisplayModel(
                    id = it.pageId,
                    title = it.title ?: "",
                    description = it.description ?: "",
                    thumbnail = it.thumbnail?.source,
                    isBookmarked = bookmarks.any { bookmark -> it.pageId == bookmark.id }
                )
            }
            HomeTab.Bookmarks -> bookmarks.map {
                ArticleDisplayModel(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    thumbnail = it.thumbnail,
                    isBookmarked = true
                )
            }
        }
    }

    fun changeSearchText(text: String) {
        _searchText.value = text
        searchWiki(text)
    }

    fun selectTab(tab: HomeTab) {
        _selectedTab.value = tab
    }

    private fun searchWiki(text: String, startFromIndex: Int = 0) {
        viewModelScope.launch {
            repository.getSearchList(text, RESULTS_PAGE_SIZE, startFromIndex)
        }
    }

    fun handleBookmark(article: ArticleDisplayModel) {
        val dbArticle = ArticleDatabaseModel(
            id = article.id,
            title = article.title,
            description = article.description,
            thumbnail = article.thumbnail
        )
        viewModelScope.launch {
            if (article.isBookmarked) {
                repository.removeBookmark(dbArticle)
            } else {
                repository.addBookmark(dbArticle)
            }
        }
    }

    fun loadMoreResults() {
        val tab = selectedTab.value
        if (tab != HomeTab.Search) return
        val text = searchText.value
        val startFromIndex = searchResultList.value.size
        searchWiki(text, startFromIndex)
    }
}
