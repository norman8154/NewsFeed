package com.norman.newsfeed.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.norman.newsfeed.base.noIndicationClickable
import com.norman.resource.theme.Body
import com.norman.resource.theme.SubTitleBold
import com.norman.resource.theme.PrimaryGreen

@Composable
fun EmptyView(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 32.dp)
    ) {
        Spacer(Modifier.weight(1f))
        Text(
            text = text,
            style = TextStyle.Body,
            color = Color.Gray,
            textAlign = TextAlign.Center,
        )
    }
}
