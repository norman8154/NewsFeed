package com.norman.newsfeed.ui.main

import com.norman.newsfeed.navigation.FeedRoute
import com.norman.newsfeed.navigation.SavedRoute
import kotlin.reflect.KClass

private enum class MainBottomTab(
    val route: KClass<*>,
    val label: String
) {

    FEED(FeedRoute::class, "Feed"),

    SAVED(SavedRoute::class, "Saved"),

}