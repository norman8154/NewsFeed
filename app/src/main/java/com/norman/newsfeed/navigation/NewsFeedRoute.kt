package com.norman.newsfeed.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

@Serializable
data object FeedRoute

@Serializable
data object SavedRoute

@Serializable
data class ArticleDetailRoute(val articleId: Long)
