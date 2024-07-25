package com.example.mywikisearcher.di

import com.example.mywikisearcher.repository.BookmarkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object BookmarkModule {

    @Provides
    fun provideBookmarkHelper() = BookmarkHelper
}
