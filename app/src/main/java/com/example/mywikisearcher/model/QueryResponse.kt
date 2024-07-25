package com.example.mywikisearcher.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class QueryResponse(val query: Query? = null) {
    @Serializable
    data class Query(val pages: List<Page>? = null) {
        @Serializable
        @Entity
        data class Page(
            @PrimaryKey
            val pageid: Long,
            val title: String? = null,
            val description: String? = null,
            val thumbnail: Thumbnail? = null
        ) {
            @Serializable
            data class Thumbnail(val source: String)
        }
    }
}
