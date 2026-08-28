package com.norman.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.norman.room.entity.CachedArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: CachedArticleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articleList: List<CachedArticleEntity>): List<Long>

    @Query("Select * from cached_article WHERE id = :id")
    suspend fun getById(id: Long): CachedArticleEntity?

    @Query("Select * from cached_article ORDER BY publish_time DESC")
    suspend fun getAll(): List<CachedArticleEntity>

    @Query("Select * from cached_article ORDER BY publish_time DESC")
    fun getAllFlow(): Flow<List<CachedArticleEntity>>

    @Query("Delete from cached_article WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("Delete from cached_article")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(articles: List<CachedArticleEntity>) {
        deleteAll()
        insertAll(articles)
    }

}
