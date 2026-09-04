package com.norman.newsfeed.pojo

sealed class FeedListItem {

    data class Article(val articleBO: ArticleBO): FeedListItem()

}