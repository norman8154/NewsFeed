package com.norman.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.norman.room.entity.SavedArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: SavedArticleEntity): Long

    @Query("Select * from saved_article WHERE id = :id")
    suspend fun getById(id: Long): SavedArticleEntity?

    @Query("SELECT id FROM saved_article")
    suspend fun getSavedIdList(): List<Long>

    @Query("SELECT id FROM saved_article")
    fun getSavedIdListFlow(): Flow<List<Long>>

    @Query("Select * from saved_article ORDER BY saved_at DESC")
    suspend fun getAll(): List<SavedArticleEntity>

    @Query("Select * from saved_article ORDER BY saved_at DESC")
    fun getAllFlow(): Flow<List<SavedArticleEntity>>

    @Query("Delete from saved_article WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("Delete from saved_article")
    suspend fun deleteAll()

}