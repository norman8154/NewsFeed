package com.norman.newsfeed.pojo

sealed class FeedListItem {

    class Article(val articleBO: ArticleBO): FeedListItem()

}