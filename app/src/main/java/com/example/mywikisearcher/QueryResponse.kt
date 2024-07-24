package com.example.mywikisearcher

import kotlinx.serialization.Serializable

@Serializable
class QueryResponse {
    var query: Query? = null

    @Serializable
    class Query {
        var pages: List<Page>? = null

        @Serializable
        class Page {
            var title: String? = null
            var description: String? = null
            var thumbnail: Thumbnail? = null

            @Serializable
            class Thumbnail {
                var source: String? = null
            }
        }
    }
}
