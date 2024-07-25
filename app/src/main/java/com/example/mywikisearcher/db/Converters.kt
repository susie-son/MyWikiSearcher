package com.example.mywikisearcher.db

import androidx.room.TypeConverter
import com.example.mywikisearcher.model.QueryResponse

class Converters {
    @TypeConverter
    fun fromThumbnail(thumbnail: QueryResponse.Query.Page.Thumbnail): String {
        return thumbnail.source
    }

    @TypeConverter
    fun toThumbnail(source: String): QueryResponse.Query.Page.Thumbnail {
        return QueryResponse.Query.Page.Thumbnail(source)
    }
}
