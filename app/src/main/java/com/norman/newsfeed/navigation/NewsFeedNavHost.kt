package com.norman.newsfeed.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.norman.newsfeed.composable.LocalSnackbarHostState
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
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}
