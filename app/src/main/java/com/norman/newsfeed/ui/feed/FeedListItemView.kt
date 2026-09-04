package com.norman.newsfeed.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.norman.newsfeed.base.localizeString
import com.norman.newsfeed.base.noIndicationClickable
import com.norman.newsfeed.base.toArticleTime
import com.norman.newsfeed.base.toDateTimeString
import com.norman.newsfeed.composable.BasicImage
import com.norman.newsfeed.pojo.ArticleBO
import com.norman.newsfeed.pojo.ServiceCardBO
import com.norman.newsfeed.pojo.WeatherBO
import com.norman.resource.R
import com.norman.resource.theme.Body
import com.norman.resource.theme.Caption
import com.norman.resource.theme.Headline
import com.norman.resource.theme.SecondaryGreen
import com.norman.resource.theme.SubTitleBold
import kotlinx.collections.immutable.PersistentList

enum class FeedListItemType {
    ARTICLE, WEATHER, SERVICE_CARD
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

@Composable
fun ServiceCardListItemView(
    serviceCardList: PersistentList<ServiceCardBO>,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(
            items = serviceCardList,
            key = { it.id },
        ) { serviceCard ->
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(0.75f)
                    .background(Color.Gray, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                BasicImage(
                    imageUrl = serviceCard.thumbnail,
                    modifier = Modifier
                        .matchParentSize()
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

                Column(
                    modifier = Modifier
                        .matchParentSize()
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = serviceCard.title,
                        style = TextStyle.Body,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                    Spacer(Modifier.size(12.dp))
                }
            }
        }
    }
}

@Composable
fun WeatherListItemView(
    weatherBO: WeatherBO,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.size(32.dp))
        Image(
            painter = when (weatherBO.weatherCode) {
                in 0 .. 3 -> painterResource(R.drawable.icon_sunny)

                in 4 .. 57 -> painterResource(R.drawable.icon_cloudy)

                else -> painterResource(R.drawable.icon_rainy)
            },
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(40.dp)
        )
        Spacer(Modifier.size(16.dp))
        Text(
            text = "${weatherBO.temperature}${weatherBO.temperatureUnit}",
            style = TextStyle.Headline,
            color = Color.White,
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = "${stringResource(R.string.apparent_temperate)} ${weatherBO.apparentTemperature}${weatherBO.temperatureUnit}",
            style = TextStyle.Body,
            color = Color.White,
        )
        Spacer(Modifier.size(8.dp))
        HorizontalDivider(color = Color.Gray)
        Spacer(Modifier.size(8.dp))
        Text(
            text = localizeString(
                stringResource(R.string.last_update_time),
                hashMapOf("update_time" to weatherBO.updateTime.toDateTimeString())
            ),
            style = TextStyle.Caption,
            color = Color.LightGray,
            modifier = Modifier
                .align(Alignment.End)
        )
        Spacer(Modifier.size(12.dp))
    }
}
