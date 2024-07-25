package com.example.mywikisearcher.model

data class Article(
    val pageId: Long,
    val title: String?,
    val description: String?,
    val thumbnail: String?,
    val isBookmarked: Boolean
)
