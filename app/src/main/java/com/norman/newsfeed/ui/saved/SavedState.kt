package com.norman.newsfeed.ui.saved

import com.norman.newsfeed.base.UiState
import com.norman.newsfeed.pojo.ArticleBO
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class SavedState(
    val articleList: PersistentList<ArticleBO>,
): UiState {
    companion object {
        val initial = SavedState(
            articleList = persistentListOf()
        )
    }
}
