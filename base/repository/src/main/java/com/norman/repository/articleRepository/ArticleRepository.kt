package com.norman.repository.articleRepository

import com.norman.model.api.article.response.ArticleResponse
import com.norman.model.api.articleList.response.ArticleListResponse

interface ArticleRepository {

    suspend fun fetchArticleList(limit: Int, offset: Int): Result<ArticleListResponse>

    suspend fun fetchArticle(id: Long): Result<ArticleResponse>

}
