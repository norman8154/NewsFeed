package com.norman.repository.freshnessRepository

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class FeedSourceTest {

    private val now = TimeUnit.DAYS.toMillis(20_000)

    @Test
    fun `never fetched is stale`() {
        assertTrue(FeedSource.ARTICLE.isStale(lastFetchedAt = 0L, now = now))
    }

    @Test
    fun `article is fresh within 30 minutes`() {
        assertFalse(FeedSource.ARTICLE.isStale(now - TimeUnit.MINUTES.toMillis(29), now))
    }

    @Test
    fun `article is stale after 30 minutes`() {
        assertTrue(FeedSource.ARTICLE.isStale(now - TimeUnit.MINUTES.toMillis(30), now))
    }

    @Test
    fun `weather expires earlier than article at the same timestamp`() {
        val lastFetchedAt = now - TimeUnit.MINUTES.toMillis(5)

        assertTrue(FeedSource.WEATHER.isStale(lastFetchedAt, now))
        assertFalse(FeedSource.ARTICLE.isStale(lastFetchedAt, now))
    }

    @Test
    fun `service card stays fresh within 6 hours`() {
        assertFalse(FeedSource.SERVICE_CARD.isStale(now - TimeUnit.HOURS.toMillis(5), now))
    }

    @Test
    fun `service card is stale after 6 hours`() {
        assertTrue(FeedSource.SERVICE_CARD.isStale(now - TimeUnit.HOURS.toMillis(6), now))
    }

    @Test
    fun `timestamp in the future is treated as stale`() {
        assertTrue(FeedSource.ARTICLE.isStale(now + TimeUnit.MINUTES.toMillis(1), now))
    }
}
