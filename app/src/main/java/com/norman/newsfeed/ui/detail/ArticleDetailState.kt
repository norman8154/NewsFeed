package com.norman.newsfeed.ui.detail

import com.norman.newsfeed.base.UiState

data class ArticleDetailState(
    val temp: String
): UiState {
    companion object {
        val initial = ArticleDetailState(
            temp = ""
        )
    }
}
