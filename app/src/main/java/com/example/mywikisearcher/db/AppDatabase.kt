package com.example.mywikisearcher.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mywikisearcher.model.ArticleDatabaseModel
import com.example.mywikisearcher.repository.BookmarkDao

@Database(entities = [ArticleDatabaseModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}
