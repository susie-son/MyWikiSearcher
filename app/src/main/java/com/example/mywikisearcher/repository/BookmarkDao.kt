package com.example.mywikisearcher.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mywikisearcher.model.QueryResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM page")
    fun getAllBookmarks(): Flow<List<QueryResponse.Query.Page>>

    @Insert
    suspend fun insertBookmark(page: QueryResponse.Query.Page)

    @Delete
    suspend fun deleteBookmark(page: QueryResponse.Query.Page)
}
