package com.norman.repository.articleRepository

import com.norman.model.api.article.response.ArticleResponse
import com.norman.model.api.articleList.response.ArticleListResponse
import com.norman.retrofit.api.ArticleApi
import com.norman.room.dao.CachedArticleDao
import com.norman.room.dao.SavedArticleDao
import com.norman.room.entity.CachedArticleEntity
import com.norman.room.entity.SavedArticleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleApi: ArticleApi,
    private val cachedArticleDao: CachedArticleDao,
    private val savedArticleDao: SavedArticleDao,
): ArticleRepository {

    override suspend fun fetchArticleList(
        limit: Int,
        offset: Int
    ): Result<ArticleListResponse> = runCatching {
        articleApi.fetchArticleList(limit, offset)
    }

    override suspend fun fetchArticle(id: Long): Result<ArticleResponse> = runCatching {
        articleApi.fetchArticle(id)
    }

    override suspend fun getCachedArticleById(id: Long): CachedArticleEntity? = cachedArticleDao.getById(id)

    override suspend fun getAllCachedArticle(): List<CachedArticleEntity> = cachedArticleDao.getAll()

    override suspend fun replaceAllCachedArticle(articles: List<CachedArticleEntity>) = cachedArticleDao.replaceAll(articles)

    override suspend fun insertSavedArticle(article: SavedArticleEntity): Long = savedArticleDao.insert(article)

    override suspend fun getSavedArticleById(id: Long): SavedArticleEntity? = savedArticleDao.getById(id)

    override suspend fun getSavedArticleIdList(): List<Long> = savedArticleDao.getSavedIdList()

    override fun getSavedArticleIdListFlow(): Flow<List<Long>> = savedArticleDao.getSavedIdListFlow()

    override fun getAllSavedArticleFlow(): Flow<List<SavedArticleEntity>> = savedArticleDao.getAllFlow()

    override suspend fun deleteSavedArticleById(id: Long) = savedArticleDao.deleteById(id)

}