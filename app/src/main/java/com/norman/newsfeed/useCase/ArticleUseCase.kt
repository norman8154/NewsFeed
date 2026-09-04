package com.norman.newsfeed.useCase

import com.norman.model.api.articleList.response.ArticleListResponse
import com.norman.newsfeed.base.or
import com.norman.newsfeed.base.toEpochMilli
import com.norman.newsfeed.pojo.ArticleBO
import com.norman.repository.articleRepository.ArticleRepository
import com.norman.room.entity.SavedArticleEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {

    fun convertArticleListResponseToArticleBO(
        response: ArticleListResponse,
        savedIdList: List<Long> = listOf(),
    ): List<ArticleBO> {
        val savedIdSet = savedIdList.toSet()

        return response.results.map { article ->
            ArticleBO(
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
                isSaved = article.id in savedIdSet,
            )
        }
    }

    fun convertSavedArticleEntityToArticleBO(
        savedArticleList: List<SavedArticleEntity>,
    ): List<ArticleBO> {
        return savedArticleList.map { savedArticle ->
            ArticleBO(
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
}
