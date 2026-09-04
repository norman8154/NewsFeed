package com.norman.repository.freshnessRepository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.freshnessDataStore by preferencesDataStore(name = "freshness")

class FreshnessRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : FreshnessRepository {

    override suspend fun getLastFetchedAt(source: FeedSource): Long {
        return context.freshnessDataStore.data.first()[source.preferenceKey] ?: 0L
    }

    override suspend fun setLastFetchedAt(source: FeedSource, lastFetchedAt: Long) {
        context.freshnessDataStore.edit { preferences ->
            preferences[source.preferenceKey] = lastFetchedAt
        }
    }

    override fun isStale(source: FeedSource, lastFetchedAt: Long, now: Long): Boolean {
        return source.isStale(lastFetchedAt = lastFetchedAt, now = now)
    }

    private val FeedSource.preferenceKey
        get() = longPreferencesKey("last_fetched_at_${name.lowercase()}")
}
