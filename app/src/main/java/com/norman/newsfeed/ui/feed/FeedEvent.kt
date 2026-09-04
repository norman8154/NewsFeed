package com.norman.newsfeed.ui.feed

import com.norman.newsfeed.base.UiEvent
import com.norman.newsfeed.pojo.ToastType

sealed class FeedEvent: UiEvent {

    class OnNavigateToArticleDetail(val articleId: Long): FeedEvent()

    class OnShowToast(val toastType: ToastType) : FeedEvent()
}
