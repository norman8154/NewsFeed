package com.norman.newsfeed.pojo

data class ServiceCardBO(
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
