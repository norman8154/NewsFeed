package com.norman.newsfeed.ui.detail

import com.norman.newsfeed.base.UiIntent
import com.norman.newsfeed.pojo.ArticleBO

sealed class ArticleDetailIntent: UiIntent {

    class Init(val articleId: Long) : ArticleDetailIntent()

    class OnUserClickSaveArticle(val articleBO: ArticleBO) : ArticleDetailIntent()

}
