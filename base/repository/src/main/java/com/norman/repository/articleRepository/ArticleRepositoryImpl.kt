package com.norman.repository.articleRepository

import com.norman.model.api.article.response.ArticleResponse
import com.norman.model.api.articleList.response.ArticleListResponse
import com.norman.retrofit.api.ArticleApi
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleApi: ArticleApi,
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
}