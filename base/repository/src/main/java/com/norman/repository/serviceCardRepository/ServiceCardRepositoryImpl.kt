package com.norman.repository.serviceCardRepository

import com.norman.model.api.serviceCard.response.ServiceCardListResponse
import com.norman.retrofit.api.ServiceCardApi
import javax.inject.Inject

class ServiceCardRepositoryImpl @Inject constructor(
    private val serviceCardApi: ServiceCardApi,
) : ServiceCardRepository {

    override suspend fun fetchServiceCardList(
        limit: Int,
        skip: Int,
    ): Result<ServiceCardListResponse> = runCatching {
        serviceCardApi.fetchServiceCardList(limit, skip)
    }
}
