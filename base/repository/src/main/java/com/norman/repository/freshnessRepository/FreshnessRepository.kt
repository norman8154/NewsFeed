package com.norman.repository.freshnessRepository

interface FreshnessRepository {

    suspend fun getLastFetchedAt(source: FeedSource): Long

    suspend fun setLastFetchedAt(source: FeedSource, lastFetchedAt: Long = System.currentTimeMillis())

    fun isStale(source: FeedSource, lastFetchedAt: Long, now: Long = System.currentTimeMillis()): Boolean
}
