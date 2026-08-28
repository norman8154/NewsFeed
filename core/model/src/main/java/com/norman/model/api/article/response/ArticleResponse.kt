package com.norman.model.api.article.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(
    val id: Long,
    val title: String,
    val authors: List<AuthorResponse> = emptyList(),
    val url: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("news_site") val newsSite: String,
    val summary: String,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val featured: Boolean?,
    val launches: List<LaunchResponse> = emptyList(),
    val events: List<EventResponse> = emptyList(),
)

@Serializable
data class AuthorResponse(
    val name: String,
    val socials: SocialsResponse? = null,
)

@Serializable
data class SocialsResponse(
    val x: String? = null,
    val youtube: String? = null,
    val instagram: String? = null,
    val linkedin: String? = null,
    val mastodon: String? = null,
    val bluesky: String? = null,
)

@Serializable
data class LaunchResponse(
    @SerialName("launch_id") val launchId: String,
    val provider: String,
)

@Serializable
data class EventResponse(
    @SerialName("event_id") val eventId: Long,
    val provider: String,
)
