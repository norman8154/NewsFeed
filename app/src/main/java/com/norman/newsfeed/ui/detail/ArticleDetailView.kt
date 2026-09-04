package com.norman.newsfeed.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.norman.newsfeed.composable.LocalSnackbarHostState
import com.norman.newsfeed.pojo.messageResId
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.norman.newsfeed.base.ifNonNull
import com.norman.newsfeed.base.noIndicationClickable
import com.norman.newsfeed.base.toArticleTime
import com.norman.newsfeed.base.topSystemInsetsPadding
import com.norman.newsfeed.composable.BasicImage
import com.norman.newsfeed.pojo.ArticleBO
import com.norman.resource.R
import com.norman.resource.theme.Body
import com.norman.resource.theme.Headline
import com.norman.resource.theme.SecondaryGreen
import com.norman.resource.theme.SubTitleBold

@Composable
fun ArticleDetailView(
    viewModel: ArticleDetailViewModel,
    articleId: Long,
    onBackClicked: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current

    LaunchedEffect(articleId) {
        viewModel.sendIntent(ArticleDetailIntent.Init(articleId))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ArticleDetailEvent.OnShowToast -> {
                    snackbarHostState.showSnackbar(context.getString(event.toastType.messageResId))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        state.articleBO.ifNonNull { articleBO ->
            ArticleContentView(
                articleBO = articleBO,
                modifier = Modifier
                    .matchParentSize()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .topSystemInsetsPadding()
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            ) {
                Spacer(Modifier.size(16.dp))
                Image(
                    painter = painterResource(R.drawable.icon_chevron_back),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .noIndicationClickable {
                            onBackClicked()
                        }
                        .padding(6.dp)
                )
                Spacer(Modifier.weight(1f))
                Image(
                    painter = if (articleBO.isSaved) {
                        painterResource(R.drawable.icon_bookmark_filled)
                    } else {
                        painterResource(R.drawable.icon_bookmark)
                    },
                    contentDescription = null,
                    colorFilter = if (articleBO.isSaved) {
                        ColorFilter.tint(Color.SecondaryGreen)
                    } else {
                        ColorFilter.tint(Color.White)
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .noIndicationClickable {
                            viewModel.sendIntent(ArticleDetailIntent.OnUserClickSaveArticle(articleBO))
                        }
                        .padding(6.dp)
                )
                Spacer(Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun ArticleContentView(
    articleBO: ArticleBO,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            BasicImage(
                imageUrl = articleBO.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.78f)
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.5f)
                        )
                    )
            )

            Text(
                text = articleBO.title,
                style = TextStyle.Headline,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .fillMaxWidth()
            )
        }
        Spacer(Modifier.size(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(Modifier.size(16.dp))
            Text(
                text = articleBO.newsSite,
                style = TextStyle.SubTitleBold,
                color = Color.Black
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = articleBO.publishTime.toArticleTime(),
                style = TextStyle.Body,
                color = Color.Gray
            )
        }
        Spacer(Modifier.size(8.dp))
        Text(
            text = articleBO.summary,
            style = TextStyle.Body,
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
    }
}
