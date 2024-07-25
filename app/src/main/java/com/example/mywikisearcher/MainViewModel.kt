package com.example.mywikisearcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val service: Service,
    private val bookmarkHelper: BookmarkHelper
): ViewModel() {

    private val _searchResultList = MutableStateFlow<List<QueryResponse.Query.Page>>(emptyList())
    val searchResultList: StateFlow<List<QueryResponse.Query.Page>> = _searchResultList.asStateFlow()

    val bookmarksList = bookmarkHelper.bookmarks

    fun searchWiki(text: CharSequence?, startFromIndex: Int = 0) {
        if (text.isNullOrEmpty()) return
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
                _searchResultList.value = emptyList()
            }

            _searchResultList.value = finalList
        }
    }

    fun handleBookmark(item: QueryResponse.Query.Page): Boolean {
        return bookmarkHelper.toggleBookmark(item)
    }
}
