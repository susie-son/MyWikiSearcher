package com.example.mywikisearcher.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleDatabaseModel(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String?
)
