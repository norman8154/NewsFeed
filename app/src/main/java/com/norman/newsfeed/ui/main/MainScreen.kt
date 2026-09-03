package com.norman.newsfeed.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.norman.newsfeed.base.noIndicationClickable
import com.norman.newsfeed.navigation.FeedRoute
import com.norman.newsfeed.navigation.SavedRoute
import com.norman.newsfeed.ui.feed.FeedView
import com.norman.newsfeed.ui.saved.SavedView
import com.norman.resource.R
import com.norman.resource.theme.Caption

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    onArticleClicked: (id: Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        NavHost(
            navController = navController,
            startDestination = FeedRoute,
            modifier = Modifier
                .weight(1f),
        ) {
            composable<FeedRoute> {
                FeedView(
                    onArticleClicked = onArticleClicked
                )
            }

            composable<SavedRoute> {
                SavedView()
            }
        }

        val backStack by navController.currentBackStackEntryAsState()

        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(60.dp)
        ) {
            MainBottomTab.entries.forEach { tab ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .noIndicationClickable {
                            navController.navigate(tab.route)
                        }
                ) {
                    HorizontalDivider()
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = when (tab) {
                            MainBottomTab.FEED -> painterResource(R.drawable.icon_article)

                            MainBottomTab.SAVED -> painterResource(R.drawable.icon_bookmark)
                        },
                        contentDescription = null,
                        colorFilter = if (backStack?.destination?.hasRoute(tab.route::class) == true) {
                            ColorFilter.tint(Color.Black)
                        } else {
                            ColorFilter.tint(Color.LightGray)
                        },
                        modifier = Modifier
                            .size(28.dp)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = when (tab) {
                            MainBottomTab.FEED -> stringResource(R.string.article)

                            MainBottomTab.SAVED -> stringResource(R.string.saved)
                        },
                        style = TextStyle.Caption,
                        color = if (backStack?.destination?.hasRoute(tab.route::class) == true) {
                            Color.Black
                        } else {
                            Color.LightGray
                        }
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMainScreen() {
    MainScreen {

    }
}
