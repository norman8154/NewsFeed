package com.norman.newsfeed.useCase

import com.norman.model.api.articleList.response.ArticleListResponse
import com.norman.newsfeed.base.or
import com.norman.newsfeed.base.toEpochMilli
import com.norman.newsfeed.pojo.ArticleBO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleUseCase @Inject constructor() {

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
                imageUrl = article.imageUrl,
                publishTime = article.publishedAt.toEpochMilli(),
                updateTime = article.updatedAt.toEpochMilli(),
                summary = article.summary,
                isFeatured = article.featured.or(false),
                isSaved = article.id in savedIdSet,
            )
        }
    }

}
