package com.example.mywikisearcher.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryResponse(val query: Query? = null) {
    @Serializable
    data class Query(val pages: List<Page>? = null) {
        @Serializable
        data class Page(
            @SerialName("pageid")
            val pageId: Int,
            val title: String? = null,
            val description: String? = null,
            val thumbnail: Thumbnail? = null,
            val coordinates: List<Coordinate>? = null
        ) {
            @Serializable
            data class Thumbnail(val source: String? = null)

            @Serializable
            data class Coordinate(
                val lat: Double? = null,
                val lon: Double? = null
            )
        }
    }
}

fun QueryResponse.Query.Page.toArticleState(bookmarked: Boolean): ArticleState {
    return ArticleState(
        id = pageId,
        title = title ?: "",
        description = description,
        thumbnail = thumbnail?.source,
        bookmarked = bookmarked,
        coordinate = coordinates?.firstOrNull()?.toArticleStateCoordinate()
    )
}

fun QueryResponse.Query.Page.Coordinate?.toArticleStateCoordinate(): ArticleState.Coordinate? {
    if (this == null || lat == null || lon == null) return null
    return ArticleState.Coordinate(lat, lon)
}
