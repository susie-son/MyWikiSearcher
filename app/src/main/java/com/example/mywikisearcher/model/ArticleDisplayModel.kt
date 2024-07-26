package com.example.mywikisearcher.model

data class ArticleDisplayModel(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: String?,
    val isBookmarked: Boolean,
    val coordinate: Pair<Double, Double>?
)
