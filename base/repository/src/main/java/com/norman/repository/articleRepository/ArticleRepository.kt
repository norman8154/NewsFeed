package com.norman.repository.articleRepository

import com.norman.model.api.article.response.ArticleResponse
import com.norman.model.api.articleList.response.ArticleListResponse
import com.norman.room.entity.CachedArticleEntity
import com.norman.room.entity.SavedArticleEntity
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    suspend fun fetchArticleList(limit: Int, offset: Int): Result<ArticleListResponse>

    suspend fun fetchArticle(id: Long): Result<ArticleResponse>

    suspend fun getCachedArticleById(id: Long): CachedArticleEntity?

    fun getAllCachedArticleFlow(): Flow<List<CachedArticleEntity>>

    suspend fun replaceAllCachedArticle(articles: List<CachedArticleEntity>)

    suspend fun insertSavedArticle(article: SavedArticleEntity): Long

    suspend fun getSavedArticleById(id: Long): SavedArticleEntity?

    suspend fun getSavedArticleIdList(): List<Long>

    fun getSavedArticleIdListFlow(): Flow<List<Long>>

    fun getAllSavedArticleFlow(): Flow<List<SavedArticleEntity>>

    suspend fun deleteSavedArticleById(id: Long)

}
