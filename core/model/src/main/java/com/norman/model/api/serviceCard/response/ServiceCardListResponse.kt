package com.norman.model.api.serviceCard.response

import kotlinx.serialization.Serializable

@Serializable
data class ServiceCardListResponse(
    val products: List<ServiceCardResponse> = emptyList(),
    val total: Int,
    val skip: Int,
    val limit: Int,
)

@Serializable
data class ServiceCardResponse(
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Float,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val thumbnail: String,
)
