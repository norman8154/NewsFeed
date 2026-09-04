package com.norman.repository.serviceCardRepository

import com.norman.model.api.serviceCard.response.ServiceCardListResponse

interface ServiceCardRepository {

    suspend fun fetchServiceCardList(
        limit: Int,
        skip: Int,
    ): Result<ServiceCardListResponse>
}
