package com.example.mywikisearcher.di

import android.content.Context
import androidx.room.Room
import com.example.mywikisearcher.util.Constants.DATABASE_NAME
import com.example.mywikisearcher.db.AppDatabase
import com.example.mywikisearcher.db.Converters
import com.example.mywikisearcher.repository.BookmarkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTypeConverter(json: Json): Converters {
        return Converters(json)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext applicationContext: Context,
        typeConverter: Converters
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .addTypeConverter(typeConverter)
            .build()
    }

    @Provides
    fun provideBookmarkDao(appDatabase: AppDatabase): BookmarkDao {
        return appDatabase.bookmarkDao()
    }
}
