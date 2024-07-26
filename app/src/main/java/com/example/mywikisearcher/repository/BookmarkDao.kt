package com.example.mywikisearcher.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mywikisearcher.model.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM articles")
    fun getAllBookmarks(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceBookmark(article: ArticleEntity)

    @Delete
    suspend fun deleteBookmark(article: ArticleEntity)
}
