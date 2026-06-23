package com.example.news.domain.model

data class NewsItem(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val authorName: String,
    val authorAvatarUrl: String?,
    val cityName: String,
    val sphereName: String,
    val createdAt: String,
)