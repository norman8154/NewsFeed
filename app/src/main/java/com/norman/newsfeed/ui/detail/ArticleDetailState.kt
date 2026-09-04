package com.norman.newsfeed.ui.detail

import com.norman.newsfeed.base.UiState
import com.norman.newsfeed.pojo.ArticleBO

data class ArticleDetailState(
    val articleBO: ArticleBO?,
    val isArticleFetching: Boolean,
): UiState {
    companion object {
        val initial = ArticleDetailState(
            articleBO = null,
            isArticleFetching = false
        )
    }
}
