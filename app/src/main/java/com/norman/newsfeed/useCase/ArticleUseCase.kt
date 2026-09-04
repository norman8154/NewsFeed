package com.norman.newsfeed.useCase

import com.norman.model.api.article.response.ArticleResponse
import com.norman.model.api.articleList.response.ArticleListResponse
import com.norman.newsfeed.base.or
import com.norman.newsfeed.base.toEpochMilli
import com.norman.newsfeed.pojo.ArticleBO
import com.norman.repository.articleRepository.ArticleRepository
import com.norman.room.entity.CachedArticleEntity
import com.norman.room.entity.SavedArticleEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository,
) {

    fun convertArticleListResponseToArticleBO(
        response: ArticleListResponse,
        savedIdList: List<Long> = listOf(),
    ): List<ArticleBO> {
        val savedIdSet = savedIdList.toSet()

        return response.results.map { article ->
            convertArticleResponseToArticleBO(
                article = article,
                isSaved = article.id in savedIdSet,
            )
        }
    }

    fun convertArticleResponseToArticleBO(
        article: ArticleResponse,
        isSaved: Boolean,
    ): ArticleBO {
        return ArticleBO(
            id = article.id,
            title = article.title,
            authorName = article.authors.firstOrNull()?.name.orEmpty(),
            newsSite = article.newsSite,
            url = article.url,
            imageUrl = article.imageUrl,
            publishTime = article.publishedAt.toEpochMilli(),
            updateTime = article.updatedAt.toEpochMilli(),
            summary = article.summary,
            isFeatured = article.featured.or(false),
            isSaved = isSaved,
        )
    }

    fun convertSavedArticleEntityToArticleBO(
        savedArticleList: List<SavedArticleEntity>,
    ): List<ArticleBO> {
        return savedArticleList.map { savedArticle ->
            convertSavedArticleEntityToArticleBO(savedArticle = savedArticle)
        }
    }

    fun convertSavedArticleEntityToArticleBO(
        savedArticle: SavedArticleEntity,
    ): ArticleBO {
        return ArticleBO(
            id = savedArticle.id,
            title = savedArticle.title,
            authorName = savedArticle.authorName,
            newsSite = savedArticle.newsSite,
            url = savedArticle.url,
            imageUrl = savedArticle.imageUrl,
            publishTime = savedArticle.publishTime,
            updateTime = savedArticle.updateTime,
            summary = savedArticle.summary,
            isFeatured = savedArticle.isFeatured,
            isSaved = true,
        )
    }

    fun convertCachedArticleEntityToArticleBO(
        cachedArticle: CachedArticleEntity,
        isSaved: Boolean,
    ): ArticleBO {
        return ArticleBO(
            id = cachedArticle.id,
            title = cachedArticle.title,
            authorName = cachedArticle.authorName,
            newsSite = cachedArticle.newsSite,
            url = cachedArticle.url,
            imageUrl = cachedArticle.imageUrl,
            publishTime = cachedArticle.publishTime,
            updateTime = cachedArticle.updateTime,
            summary = cachedArticle.summary,
            isFeatured = cachedArticle.isFeatured,
            isSaved = isSaved,
        )
    }

    suspend fun saveArticleBOToDB(articleBO: ArticleBO): Long {
        val entity = SavedArticleEntity(
            id = articleBO.id,
            title = articleBO.title,
            authorName = articleBO.authorName,
            newsSite = articleBO.newsSite,
            url = articleBO.url,
            imageUrl = articleBO.imageUrl,
            publishTime = articleBO.publishTime,
            updateTime = articleBO.updateTime,
            summary = articleBO.summary,
            isFeatured = articleBO.isFeatured,
            savedAt = System.currentTimeMillis(),
        )

        return articleRepository.insertSavedArticle(entity)
    }

    suspend fun replaceCachedArticleList(articleList: List<ArticleBO>) {
        articleRepository.replaceAllCachedArticle(
            articles = articleList.map { convertArticleBOToCachedArticleEntity(it) }
        )
    }

    suspend fun getCachedArticleBOList(): List<ArticleBO> {
        val savedIdSet = articleRepository.getSavedArticleIdList().toSet()

        return articleRepository.getAllCachedArticle().map { cachedArticle ->
            convertCachedArticleEntityToArticleBO(
                cachedArticle = cachedArticle,
                isSaved = cachedArticle.id in savedIdSet,
            )
        }
    }

    private fun convertArticleBOToCachedArticleEntity(articleBO: ArticleBO): CachedArticleEntity {
        return CachedArticleEntity(
            id = articleBO.id,
            title = articleBO.title,
            authorName = articleBO.authorName,
            newsSite = articleBO.newsSite,
            url = articleBO.url,
            imageUrl = articleBO.imageUrl,
            publishTime = articleBO.publishTime,
            updateTime = articleBO.updateTime,
            summary = articleBO.summary,
            isFeatured = articleBO.isFeatured,
        )
    }
}
