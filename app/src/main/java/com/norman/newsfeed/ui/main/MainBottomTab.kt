package com.norman.newsfeed.ui.main

import com.norman.newsfeed.navigation.FeedRoute
import com.norman.newsfeed.navigation.Route
import com.norman.newsfeed.navigation.SavedRoute

enum class MainBottomTab(
    val route: Route,
    val label: String
) {

    FEED(FeedRoute, "Feed"),

    SAVED(SavedRoute, "Saved"),

}