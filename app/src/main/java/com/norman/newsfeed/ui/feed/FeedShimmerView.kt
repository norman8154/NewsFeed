package com.norman.newsfeed.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

private const val SHIMMER_ARTICLE_COUNT_BEFORE_SERVICE_CARD = 5

private const val SHIMMER_ARTICLE_COUNT_AFTER_SERVICE_CARD = 3

@Composable
fun FeedShimmerView(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    Column(
        modifier = modifier
            .shimmer(shimmer)
            .padding(vertical = 12.dp)
    ) {

    }
}

@Composable
fun WeatherListItemShimmerView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(220.dp)
            .background(Color.LightGray, RoundedCornerShape(12.dp))
    )
}

@Composable
fun ArticleListItemShimmerView() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Spacer(Modifier.size(24.dp))
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.size(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            ShimmerLine(widthFraction = 0.4f)
            Spacer(Modifier.size(6.dp))
            ShimmerLine(widthFraction = 1f)
            Spacer(Modifier.size(6.dp))
            ShimmerLine(widthFraction = 0.7f)
            Spacer(Modifier.size(8.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
            )
        }
        Spacer(Modifier.size(24.dp))
    }
}

@Composable
fun ServiceCardListItemShimmerView() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(0.75f)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun ShimmerLine(
    widthFraction: Float,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(14.dp)
            .background(Color.LightGray, RoundedCornerShape(4.dp))
    )
}
