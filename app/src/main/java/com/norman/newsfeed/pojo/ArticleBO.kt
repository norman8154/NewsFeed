package com.norman.newsfeed.pojo

data class ArticleBO(
    val id: Long,
    val title: String,
    val authorName: String,
    val newsSite: String,
    val url: String,
    val imageUrl: String,
    val publishTime: Long,
    val updateTime: Long,
    val summary: String,
    val isFeatured: Boolean,
    val isSaved: Boolean,
)
