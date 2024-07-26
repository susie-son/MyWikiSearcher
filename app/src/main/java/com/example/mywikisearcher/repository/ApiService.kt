package com.example.mywikisearcher.repository

import com.example.mywikisearcher.util.Constants.API_ENDPOINT
import com.example.mywikisearcher.model.QueryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(API_ENDPOINT)
    suspend fun prefixSearch(
        @Query("gpssearch") searchTerm: String,
        @Query("gpslimit") maxResults: Int,
        @Query("gpsoffset") startFromIndex: Int?,
        @Query("format") format: String = "json",
        @Query("formatversion") formatVersion: Int = 2,
        @Query("action") action: String = "query",
        @Query("redirects") redirects: String = "",
        @Query("converttitles") convertTitles: String = "",
        @Query("pageids") pageIds: String = "",
        @Query("prop") properties: String = "description|pageimages|info|coordinates",
        @Query("piprop") pageImageProperties: String = "thumbnail",
        @Query("pilicense") pageImageLicense: String = "any",
        @Query("generator") generator: String = "prefixsearch",
        @Query("gpsnamespace") gpsNamespace: Int = 0,
        @Query("inprop") inProperties: String = "displaytitle",
        @Query("pithumbsize") pageImageThumbnailSize: Int = 320,
        @Query("colimit") coordinateLimit: Int = 1,
    ): QueryResponse
}
