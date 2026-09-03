package com.norman.newsfeed.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.norman.newsfeed.navigation.FeedRoute
import com.norman.newsfeed.navigation.SavedRoute
import com.norman.newsfeed.ui.feed.FeedView
import com.norman.newsfeed.ui.saved.SavedView

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = FeedRoute,
            modifier = Modifier
                .weight(1f),
        ) {
            composable<FeedRoute> {
                FeedView()
            }

            composable<SavedRoute> {
                SavedView()
            }
        }
    }
}
