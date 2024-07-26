package com.example.mywikisearcher.repository

import com.example.mywikisearcher.model.QueryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("w/api.php?format=json&formatversion=2&action=query&redirects=&converttitles=&" +
            "&pageids=&prop=description|pageimages|info|coordinates&piprop=thumbnail&pilicense=any&" +
            "generator=prefixsearch&gpsnamespace=0&inprop=displaytitle&pithumbsize=320&colimit=1")
    suspend fun prefixSearch(@Query("gpssearch") searchTerm: String,
                             @Query("gpslimit") maxResults: Int,
                             @Query("gpsoffset") startFromIndex: Int?): QueryResponse
}
