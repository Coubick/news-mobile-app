package com.example.news.domain.repository

import com.example.news.domain.model.FilterItem
import com.example.news.utils.Result

interface NewsFilterOptionsRepository {
    suspend fun getAllCities(): Result<List<FilterItem>>

    suspend fun getAllSpheres(): Result<List<FilterItem>>
}