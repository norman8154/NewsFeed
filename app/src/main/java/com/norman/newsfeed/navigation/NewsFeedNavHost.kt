package com.norman.newsfeed.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.norman.newsfeed.ui.detail.ArticleDetailView
import com.norman.newsfeed.ui.main.MainScreen

@Composable
fun NewsFeedNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute,
    ) {
        composable<MainRoute> {
            MainScreen()
        }

        composable<ArticleDetailRoute> { backStackEntry ->

            ArticleDetailView(

            )
        }
    }
}
