package com.norman.newsfeed.ui.detail

import com.norman.newsfeed.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(

) : BaseViewModel<ArticleDetailState, ArticleDetailIntent, ArticleDetailEvent>(ArticleDetailState.initial) {

    override suspend fun handleIntent(intent: ArticleDetailIntent) {

    }

}