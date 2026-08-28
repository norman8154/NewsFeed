package com.norman.retrofit.api

import com.norman.model.api.article.response.ArticleResponse
import com.norman.model.api.articleList.response.ArticleListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleApi {

    @GET("v4/articles/")
    suspend fun fetchArticleList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): ArticleListResponse

    @GET("v4/articles/{id}/")
    suspend fun fetchArticle(
        @Path("id") id: Long,
    ): ArticleResponse
}
