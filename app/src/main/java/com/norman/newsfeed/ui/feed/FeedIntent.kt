package com.norman.newsfeed.ui.feed

import com.norman.newsfeed.base.UiIntent

sealed class FeedIntent : UiIntent {

    data object Init : FeedIntent()

    data object OnArticleLoadMore : FeedIntent()

    data object OnRefreshing: FeedIntent()

}
