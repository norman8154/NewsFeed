package com.norman.newsfeed.pojo

import kotlinx.collections.immutable.PersistentList

sealed class FeedListItem {

    data class Article(val articleBO: ArticleBO) : FeedListItem()

    data class Weather(val weatherBO: WeatherBO) : FeedListItem()

    data class ServiceCard(val serviceCardList: PersistentList<ServiceCardBO>) : FeedListItem()

}