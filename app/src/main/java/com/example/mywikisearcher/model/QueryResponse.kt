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
            data class Thumbnail(val source: String)

            @Serializable
            data class Coordinate(
                val lat: Double,
                val lon: Double
            )
        }
    }
}
