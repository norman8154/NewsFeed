package com.norman.newsfeed.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.norman.resource.R
import com.norman.resource.theme.SubTitleBold

@Composable
fun LowInternetIndicator(
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.icon_offline),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Red),
            modifier = Modifier
                .size(20.dp)
        )
        Spacer(Modifier.size(12.dp))
        Text(
            text = stringResource(R.string.low_internet),
            style = TextStyle.SubTitleBold,
            color = Color.Red
        )
    }
}