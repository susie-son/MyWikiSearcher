package com.example.mywikisearcher

object BookmarkHelper {

    val bookmarks = mutableListOf<QueryResponse.Query.Page>()

    fun addBookmark(page: QueryResponse.Query.Page) {
        bookmarks.add(page)
    }

    fun removeBookmark(page: QueryResponse.Query.Page) {
        bookmarks.remove(page)
    }

    fun isBookmarked(page: QueryResponse.Query.Page): Boolean {
        return bookmarks.contains(page)
    }

    fun persistBookmarks() {
        // TODO
    }
}
