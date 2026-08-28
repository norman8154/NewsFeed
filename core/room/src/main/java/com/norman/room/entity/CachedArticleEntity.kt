package com.norman.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_article")
data class CachedArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author_name")
    val authorName: String,
    @ColumnInfo(name = "news_site")
    val newsSite: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "publish_time")
    val publishTime: Long,
    @ColumnInfo(name = "update_time")
    val updateTime: Long,
    @ColumnInfo(name = "summary")
    val summary: String,
    @ColumnInfo(name = "is_featured")
    val isFeatured: Boolean,
)
