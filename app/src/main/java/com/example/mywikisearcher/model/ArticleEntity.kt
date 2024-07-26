package com.example.mywikisearcher.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: String?,
    val coordinate: Coordinate?
) {
    @Serializable
    data class Coordinate(
        val latitude: Double,
        val longitude: Double
    )
}

fun ArticleEntity.toArticleState(bookmarked: Boolean): ArticleState {
    return ArticleState(
        id = id,
        title = title,
        description = description,
        thumbnail = thumbnail,
        bookmarked = bookmarked,
        coordinate = coordinate?.let { coordinate ->
            ArticleState.Coordinate(coordinate.latitude, coordinate.longitude)
        }
    )
}
