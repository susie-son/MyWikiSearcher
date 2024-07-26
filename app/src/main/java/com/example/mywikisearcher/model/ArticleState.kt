package com.example.mywikisearcher.model

data class ArticleState(
    val id: Int,
    val title: String,
    val bookmarked: Boolean,
    val description: String?,
    val thumbnail: String?,
    val coordinate: Coordinate?
) {
    data class Coordinate(
        val latitude: Double,
        val longitude: Double
    )
}

fun ArticleState.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        id = id,
        title = title,
        description = description,
        thumbnail = thumbnail,
        coordinate = coordinate?.let { coordinate ->
            ArticleEntity.Coordinate(coordinate.latitude, coordinate.longitude)
        }
    )
}
