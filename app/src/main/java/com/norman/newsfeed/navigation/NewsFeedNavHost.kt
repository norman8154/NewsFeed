package com.norman.newsfeed.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
            MainScreen(
                onArticleClicked = {
                    navController.navigate(ArticleDetailRoute(it))
                }
            )
        }

        composable<ArticleDetailRoute> { backStackEntry ->
            val route: ArticleDetailRoute = backStackEntry.toRoute()

            ArticleDetailView(
                viewModel = hiltViewModel(),
                articleId = route.articleId,
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
    }
}
