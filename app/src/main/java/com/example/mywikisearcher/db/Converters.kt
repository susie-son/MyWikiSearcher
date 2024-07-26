package com.example.mywikisearcher.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.mywikisearcher.model.ArticleEntity.Coordinate
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(
    private val json: Json
) {
    @TypeConverter
    fun coordinateToString(coordinate: Coordinate?): String? {
        return coordinate?.let { json.encodeToString(Coordinate.serializer(), it) }
    }

    @TypeConverter
    fun stringToCoordinate(value: String?): Coordinate? {
        return value?.let { json.decodeFromString(Coordinate.serializer(), it) }
    }
}
