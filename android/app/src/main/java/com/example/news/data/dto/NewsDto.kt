package com.example.news.data.dto

import java.util.Date

data class NewsDto (
    val id: Long,
    val userId: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val cityId: Long,
    val sphereId: Long,
    val createdAt: Date,
    val updatedAt: Date,
    val deletedAt: Date
)