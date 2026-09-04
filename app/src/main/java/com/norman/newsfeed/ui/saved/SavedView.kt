package com.norman.newsfeed.ui.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.norman.newsfeed.composable.LocalSnackbarHostState
import com.norman.newsfeed.pojo.messageResId
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.norman.newsfeed.base.topSystemInsetsPadding
import com.norman.newsfeed.composable.EmptyView
import com.norman.newsfeed.composable.LowInternetIndicator
import com.norman.newsfeed.pojo.ArticleBO
import com.norman.newsfeed.ui.feed.ArticleListItemView
import com.norman.newsfeed.ui.feed.FeedListItemType
import com.norman.resource.R
import com.norman.resource.theme.Headline
import com.norman.resource.theme.PrimaryGreen

@Composable
fun SavedView(
    viewModel: SavedViewModel,
    onArticleClicked: (id: Long) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SavedEvent.OnNavigateToArticleDetail -> {
                    onArticleClicked(event.articleId)
                }

                is SavedEvent.OnShowToast -> {
                    snackbarHostState.showSnackbar(context.getString(event.toastType.messageResId))
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
            text = stringResource(R.string.saved),
            style = TextStyle.Headline,
            color = Color.PrimaryGreen,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        if (state.isLowInternet) {
            LowInternetIndicator(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        SavedArticleListView(
            articleList = state.articleList,
            onArticleClicked = {
                viewModel.sendIntent(SavedIntent.OnUserClickArticle(it))
            },
            onSaveClicked = {
                viewModel.sendIntent(SavedIntent.OnUserClickSaveArticle(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun SavedArticleListView(
    articleList: List<ArticleBO>,
    onArticleClicked: (ArticleBO) -> Unit,
    onSaveClicked: (ArticleBO) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = modifier
    ) {
        if (articleList.isEmpty()) {
            item {
                EmptyView(
                    text = stringResource(R.string.saved_empty),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        } else {
            items(
                items = articleList,
                key = { it.id },
                contentType = { FeedListItemType.ARTICLE },
            ) { articleBO ->
                ArticleListItemView(
                    articleBO = articleBO,
                    onArticleClicked = {
                        onArticleClicked(it)
                    },
                    onSaveClicked = {
                        onSaveClicked(it)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSavedView() {

}
