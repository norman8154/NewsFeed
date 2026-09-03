package com.norman.newsfeed.ui.feed

import com.norman.newsfeed.base.UiState

data class FeedState(
    val temp: String
): UiState {
    companion object {
        val initial = FeedState(
            temp = ""
        )
    }
}
