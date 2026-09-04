package com.norman.newsfeed.ui.feed

import com.norman.newsfeed.base.UiIntent
import com.norman.newsfeed.pojo.ArticleBO

sealed class FeedIntent : UiIntent {

    data object Init : FeedIntent()

    data object OnArticleLoadMore : FeedIntent()

    data object OnRefreshing : FeedIntent()

    class OnUserClickSaveArticle(val articleBO: ArticleBO) : FeedIntent()

    class OnUserClickArticle(val articleBO: ArticleBO) : FeedIntent()

}
