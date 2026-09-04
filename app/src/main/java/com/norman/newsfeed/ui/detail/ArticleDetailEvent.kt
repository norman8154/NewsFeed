package com.norman.newsfeed.ui.detail

import com.norman.newsfeed.base.UiEvent
import com.norman.newsfeed.pojo.ToastType

sealed class ArticleDetailEvent: UiEvent {

    class OnShowToast(val toastType: ToastType) : ArticleDetailEvent()
}
