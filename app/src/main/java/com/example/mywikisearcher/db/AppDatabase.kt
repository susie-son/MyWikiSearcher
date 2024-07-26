package com.example.mywikisearcher.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mywikisearcher.model.QueryResponse
import com.example.mywikisearcher.repository.BookmarkDao

@Database(entities = [QueryResponse.Query.Page::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}
