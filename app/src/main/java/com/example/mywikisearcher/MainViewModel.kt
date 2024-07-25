package com.example.mywikisearcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val service: Service,
    private val bookmarkHelper: BookmarkHelper
): ViewModel() {

    val searchResultList = mutableListOf<QueryResponse.Query.Page>()

    fun searchWiki(
        text: CharSequence?,
        startFromIndex: Int = 0,
        onUpdateList: (List<QueryResponse.Query.Page>) -> Unit
    ) {
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

            if (startFromIndex == 0)
                searchResultList.clear()

            searchResultList.addAll(finalList)
            onUpdateList(searchResultList)
        }
    }

    // Returns true if the item was added to bookmarks; false otherwise
    fun handleBookmark(item: QueryResponse.Query.Page): Boolean {
        return if (bookmarkHelper.isBookmarked(item)) {
            bookmarkHelper.removeBookmark(item)
            // TODO: Update the UI
            false
        } else {
            bookmarkHelper.addBookmark(item)
            true
        }
    }
}
