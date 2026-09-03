package com.norman.newsfeed.navigation

import kotlinx.serialization.Serializable

sealed class Route

@Serializable
data object MainRoute: Route()

@Serializable
data object FeedRoute: Route()

@Serializable
data object SavedRoute: Route()

@Serializable
data class ArticleDetailRoute(val articleId: Long): Route()
