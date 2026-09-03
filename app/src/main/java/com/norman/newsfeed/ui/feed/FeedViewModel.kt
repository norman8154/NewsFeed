package com.norman.newsfeed.ui.feed

import com.norman.newsfeed.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(

): BaseViewModel<FeedState, FeedIntent, FeedEvent>(FeedState.initial) {

    override suspend fun handleIntent(intent: FeedIntent) {

    }

}
