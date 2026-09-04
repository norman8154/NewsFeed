package com.norman.newsfeed.useCase

import com.norman.model.api.serviceCard.response.ServiceCardListResponse
import com.norman.model.api.serviceCard.response.ServiceCardResponse
import com.norman.newsfeed.pojo.ServiceCardBO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceCardUseCase @Inject constructor() {

    fun convertServiceCardListResponseToServiceCardBO(
        response: ServiceCardListResponse,
    ): List<ServiceCardBO> {
        return response.products.map { serviceCard ->
            convertServiceCardResponseToServiceCardBO(serviceCard = serviceCard)
        }
    }

    fun convertServiceCardResponseToServiceCardBO(
        serviceCard: ServiceCardResponse,
    ): ServiceCardBO {
        return ServiceCardBO(
            id = serviceCard.id,
            title = serviceCard.title,
            description = serviceCard.description,
            category = serviceCard.category,
            price = serviceCard.price,
            discountPercentage = serviceCard.discountPercentage,
            rating = serviceCard.rating,
            stock = serviceCard.stock,
            thumbnail = serviceCard.thumbnail,
        )
    }
}
