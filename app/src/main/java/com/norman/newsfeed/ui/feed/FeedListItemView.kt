package com.norman.newsfeed.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.norman.newsfeed.base.noIndicationClickable
import com.norman.newsfeed.base.toArticleTime
import com.norman.newsfeed.composable.BasicImage
import com.norman.newsfeed.pojo.ArticleBO
import com.norman.resource.R
import com.norman.resource.theme.Body
import com.norman.resource.theme.SecondaryGreen
import com.norman.resource.theme.SubTitleBold

enum class FeedListItemType {
    ARTICLE
}

@Composable
fun ArticleListItemView(
    articleBO: ArticleBO,
    onArticleClicked: (ArticleBO) -> Unit,
    onSaveClicked: (ArticleBO) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .noIndicationClickable {
                onArticleClicked(articleBO)
            }
            .padding(vertical = 8.dp)
    ) {
        Spacer(Modifier.size(24.dp))
        BasicImage(
            imageUrl = articleBO.imageUrl,
            modifier = Modifier
                .size(72.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.size(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = articleBO.newsSite,
                    style = TextStyle.SubTitleBold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f, false)
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = articleBO.publishTime.toArticleTime(),
                    style = TextStyle.Body,
                    color = Color.Gray
                )
            }
            Spacer(Modifier.size(2.dp))
            Text(
                text = articleBO.title,
                style = TextStyle.Body,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.size(4.dp))
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
                    ColorFilter.tint(Color.Gray)
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .size(16.dp)
                    .noIndicationClickable {
                        onSaveClicked(articleBO)
                    }
            )
        }
        Spacer(Modifier.size(24.dp))
    }
}