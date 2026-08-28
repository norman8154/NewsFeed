package com.norman.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.norman.room.dao.CachedArticleDao
import com.norman.room.dao.SavedArticleDao
import com.norman.room.entity.CachedArticleEntity
import com.norman.room.entity.SavedArticleEntity

@Database(
    entities = [
        CachedArticleEntity::class,
        SavedArticleEntity::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class NewsFeedDatabase: RoomDatabase() {

    abstract fun getCachedArticleDao(): CachedArticleDao

    abstract fun getSavedArticleDao(): SavedArticleDao

}