package com.example.news.data.repository

import com.example.news.data.dto.NewsDto
import com.example.news.utils.Result

interface NewsRepository {
    suspend fun getNewsList(
        cityId: Long?,
        sphereId: Long?,
        limit: Int,
        offset: Int
    ): Result<List<NewsDto>>
}