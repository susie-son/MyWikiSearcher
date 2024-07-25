package com.example.mywikisearcher.repository

import com.example.mywikisearcher.model.QueryResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object BookmarkHelper {

    private val _bookmarks = MutableStateFlow<List<QueryResponse.Query.Page>>(emptyList())
    val bookmarks: StateFlow<List<QueryResponse.Query.Page>> = _bookmarks.asStateFlow()

    // Returns true if the item was added to bookmarks; false otherwise
    fun toggleBookmark(page: QueryResponse.Query.Page): Boolean {
        return if (bookmarks.value.contains(page)) {
            removeBookmark(page)
            false
        } else {
            addBookmark(page)
            true
        }
    }

    private fun addBookmark(page: QueryResponse.Query.Page) {
        _bookmarks.value += page
    }

    private fun removeBookmark(page: QueryResponse.Query.Page) {
        _bookmarks.value -= page
    }

    fun persistBookmarks() {
        // TODO
    }
}
