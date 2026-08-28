package com.norman.model.api.articleList.response

import com.norman.model.api.article.response.ArticleResponse
import kotlinx.serialization.Serializable

@Serializable
data class ArticleListResponse(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<ArticleResponse> = emptyList(),
)
