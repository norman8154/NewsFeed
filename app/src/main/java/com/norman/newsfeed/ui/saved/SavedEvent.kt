package com.norman.newsfeed.ui.saved

import com.norman.newsfeed.base.UiEvent

sealed class SavedEvent: UiEvent {

    class OnNavigateToArticleDetail(val articleId: Long): SavedEvent()

}
