package com.norman.newsfeed.ui.saved

import com.norman.newsfeed.base.UiEvent
import com.norman.newsfeed.pojo.ToastType

sealed class SavedEvent: UiEvent {

    class OnNavigateToArticleDetail(val articleId: Long): SavedEvent()

    class OnShowToast(val toastType: ToastType) : SavedEvent()
}
