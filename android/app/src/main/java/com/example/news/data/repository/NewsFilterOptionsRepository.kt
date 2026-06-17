package com.example.news.data.repository

import com.example.news.presentation.model.FilterItem
import com.example.news.utils.Result

interface NewsFilterOptionsRepository {
    suspend fun getAllCities(): Result<List<FilterItem>>

    suspend fun getAllSpheres(): Result<List<FilterItem>>
}