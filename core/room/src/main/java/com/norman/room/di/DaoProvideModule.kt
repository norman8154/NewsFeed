package com.norman.room.di

import android.content.Context
import androidx.room.Room
import com.norman.room.NewsFeedDatabase
import com.norman.room.dao.CachedArticleDao
import com.norman.room.dao.SavedArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoProvideModule {

    @Provides
    @Singleton
    fun provideCachedArticleDao(newsFeedDatabase: NewsFeedDatabase): CachedArticleDao = newsFeedDatabase.getCachedArticleDao()

    @Provides
    @Singleton
    fun provideSavedArticleDao(newsFeedDatabase: NewsFeedDatabase): SavedArticleDao = newsFeedDatabase.getSavedArticleDao()

    @Provides
    @Singleton
    fun provideNewsFeedDatabase(@ApplicationContext appContext: Context): NewsFeedDatabase {
        return Room.databaseBuilder(
            appContext,
            NewsFeedDatabase::class.java,
            "NewsFeed"
        ).build()
    }

}