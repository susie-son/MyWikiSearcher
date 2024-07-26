package com.example.mywikisearcher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywikisearcher.model.Article
import com.example.mywikisearcher.model.QueryResponse
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
): ViewModel() {

    private val searchResultList = repository.searchResultList
    private val bookmarksList = repository.bookmarkList

    private val _selectedTab = MutableStateFlow(HomeTab.Search)
    val selectedTab: StateFlow<HomeTab> = _selectedTab.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val articleList: Flow<List<Article>> = combine(searchResultList, bookmarksList, selectedTab) { search, bookmarks, tab ->
        when (tab) {
            HomeTab.Search -> search
            HomeTab.Bookmarks -> bookmarks
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
            repository.getSearchList(text, 20, startFromIndex)
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
                repository.removeBookmark(queryPage)
            } else {
                repository.addBookmark(queryPage)
            }
        }
    }
}
