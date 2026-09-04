package com.norman.newsfeed.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.norman.newsfeed.base.topSystemInsetsPadding
import com.norman.newsfeed.pojo.ArticleBO
import com.norman.newsfeed.pojo.FeedListItem
import com.norman.resource.R
import com.norman.resource.theme.Headline
import com.norman.resource.theme.PrimaryGreen

@Composable
fun FeedView(
    viewModel: FeedViewModel,
    onArticleClicked: (id: Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(FeedIntent.Init)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is FeedEvent.OnNavigateToArticleDetail -> {
                    onArticleClicked(event.articleId)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .topSystemInsetsPadding()
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = stringResource(R.string.article),
            style = TextStyle.Headline,
            color = Color.PrimaryGreen,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        FeedListView(
            feedList = state.feedList,
            isArticleHasMore = state.isArticleHasMore,
            isArticleFetching = state.isArticleFetching,
            onArticleClicked = {
                viewModel.sendIntent(FeedIntent.OnUserClickArticle(it))
            },
            onSaveClicked = {
                viewModel.sendIntent(FeedIntent.OnUserClickSaveArticle(it))
            },
            onRefreshing = {
                viewModel.sendIntent(FeedIntent.OnRefreshing)
            },
            onLoadMore = {
                viewModel.sendIntent(FeedIntent.OnArticleLoadMore)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        )
    }
}

@Composable
private fun FeedListView(
    feedList: List<FeedListItem>,
    isArticleHasMore: Boolean,
    isArticleFetching: Boolean,
    onArticleClicked: (ArticleBO) -> Unit,
    onSaveClicked: (ArticleBO) -> Unit,
    onRefreshing: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = modifier
            .pullToRefresh(
                isRefreshing = false,
                state = pullToRefreshState
            ) {
                onRefreshing()
            }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 12.dp),
            modifier = Modifier
                .matchParentSize()
        ) {
            itemsIndexed(
                feedList,
                contentType = { _, item ->
                    when (item) {
                        is FeedListItem.Article -> FeedListItemType.ARTICLE

                        is FeedListItem.Weather -> FeedListItemType.WEATHER

                        is FeedListItem.ServiceCard -> FeedListItemType.SERVICE_CARD
                    }
                },
            ) { index, item ->

                LaunchedEffect(
                    index,
                    feedList,
                    isArticleHasMore,
                    isArticleFetching
                ) {
                    if (isArticleHasMore && !isArticleFetching && feedList.size - index < 3) {
                        onLoadMore()
                    }
                }

                when (item) {
                    is FeedListItem.Article -> {
                        ArticleListItemView(
                            articleBO = item.articleBO,
                            onArticleClicked = {
                                onArticleClicked(it)
                            },
                            onSaveClicked = {
                                onSaveClicked(it)
                            }
                        )
                    }

                    is FeedListItem.Weather -> {
                        WeatherListItemView(weatherBO = item.weatherBO)
                    }

                    is FeedListItem.ServiceCard -> {
                        ServiceCardListItemView(serviceCardList = item.serviceCardList)
                    }
                }
            }
        }

        Indicator(
            state = pullToRefreshState,
            isRefreshing = false,
            containerColor = Color.LightGray,
            color = Color.PrimaryGreen,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

@Preview
@Composable
private fun PreviewFeedView() {

}
