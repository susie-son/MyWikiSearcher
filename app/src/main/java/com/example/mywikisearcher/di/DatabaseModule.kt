package com.example.mywikisearcher.di

import android.content.Context
import androidx.room.Room
import com.example.mywikisearcher.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabase(applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "mywikisearcher-db"
        ).build()
    }
}
