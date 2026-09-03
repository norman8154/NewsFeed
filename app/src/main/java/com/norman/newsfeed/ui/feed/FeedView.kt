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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.norman.newsfeed.base.topSystemInsetsPadding
import com.norman.resource.R
import com.norman.resource.theme.Headline
import com.norman.resource.theme.PrimaryGreen

@Composable
fun FeedView(
    onArticleClicked: (id: Long) -> Unit
) {
    val viewModel = hiltViewModel<FeedViewModel>()
    val state by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(FeedIntent.Init)
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pullToRefresh(
                    isRefreshing = false,
                    state = pullToRefreshState
                ) {
                    viewModel.sendIntent(FeedIntent.OnRefreshing)
                }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier
                    .matchParentSize()
            ) {
                itemsIndexed(
                    state.articleList,
                    contentType = { _, _ -> FeedListItemType.ARTICLE },
                ) { index, articleBO ->

                    LaunchedEffect(
                        index,
                        state.articleList,
                        state.isArticleHasMore,
                        state.isArticleFetching
                    ) {
                        if (state.isArticleHasMore && !state.isArticleFetching && state.articleList.size - index < 3) {
                            viewModel.sendIntent(FeedIntent.OnArticleLoadMore)
                        }
                    }

                    ArticleListItemView(
                        articleBO = articleBO,
                        onArticleClicked = {
                            onArticleClicked(it.id)
                        },
                        onSaveClicked = {

                        }
                    )
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
}

@Preview
@Composable
private fun PreviewFeedView() {
    FeedView {
        
    }
}
