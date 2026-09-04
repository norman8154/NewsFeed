package com.norman.newsfeed.ui.saved

import com.norman.newsfeed.base.UiIntent
import com.norman.newsfeed.pojo.ArticleBO

sealed class SavedIntent: UiIntent {

    class OnUserClickSaveArticle(val articleBO: ArticleBO) : SavedIntent()

}
