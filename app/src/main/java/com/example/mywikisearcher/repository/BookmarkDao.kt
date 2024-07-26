package com.example.mywikisearcher.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mywikisearcher.model.ArticleDatabaseModel
import com.example.mywikisearcher.model.QueryResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM articles")
    fun getAllBookmarks(): Flow<List<ArticleDatabaseModel>>

    @Insert
    suspend fun insertBookmark(article: ArticleDatabaseModel)

    @Delete
    suspend fun deleteBookmark(article: ArticleDatabaseModel)
}
