package com.norman.repository.freshnessRepository

import java.util.concurrent.TimeUnit

enum class FeedSource(val ttlMillis: Long) {

    ARTICLE(TimeUnit.MINUTES.toMillis(30)),

    WEATHER(TimeUnit.MINUTES.toMillis(5)),

    SERVICE_CARD(TimeUnit.HOURS.toMillis(6));

    fun isStale(
        lastFetchedAt: Long,
        now: Long = System.currentTimeMillis(),
    ): Boolean {
        if (lastFetchedAt <= 0L) {
            return true
        }

        if (lastFetchedAt > now) {
            return true
        }

        return now - lastFetchedAt >= ttlMillis
    }
}
