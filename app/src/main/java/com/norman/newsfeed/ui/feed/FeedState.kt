package com.norman.newsfeed.ui.feed

import com.norman.newsfeed.base.UiState
import com.norman.newsfeed.pojo.FeedListItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class FeedState(
    val feedList: PersistentList<FeedListItem>,
    val isArticleHasMore: Boolean,
    val isArticleFetching: Boolean,
    val isArticleRefreshing: Boolean,
): UiState {
    companion object {
        val initial = FeedState(
            feedList = persistentListOf(),
            isArticleHasMore = true,
            isArticleFetching = false,
            isArticleRefreshing = true
        )
    }
}
