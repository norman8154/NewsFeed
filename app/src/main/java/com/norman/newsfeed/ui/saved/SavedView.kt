package com.norman.newsfeed.ui.saved

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.norman.newsfeed.base.topSystemInsetsPadding
import com.norman.resource.theme.Headline

@Composable
fun SavedView(

) {

    Column(
        modifier = Modifier
            .topSystemInsetsPadding()
            .fillMaxSize()
    ) {
        Spacer(Modifier.weight(1f))
        Text(
            text = "Saved",
            style = TextStyle.Headline,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.weight(1f))
    }
}
