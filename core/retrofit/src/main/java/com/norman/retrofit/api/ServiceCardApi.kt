package com.norman.retrofit.api

import com.norman.model.api.serviceCard.response.ServiceCardListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceCardApi {

    @GET("products")
    suspend fun fetchServiceCardList(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
        @Query("select") fields: String = RESPONSE_FIELDS,
    ): ServiceCardListResponse

    companion object {
        const val RESPONSE_FIELDS =
            "id,title,description,category,price,discountPercentage,rating,stock,thumbnail"
    }
}
