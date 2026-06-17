package com.example.news.data.repository

import com.example.news.data.api.NewsFilterOptionsApi
import com.example.news.presentation.model.FilterItem
import com.example.news.presentation.model.toUiModel
import com.example.news.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsFilterOptionsRepositoryImpl @Inject constructor(
    private val newsFilterOptionsApi: NewsFilterOptionsApi
) : NewsFilterOptionsRepository {
    private var citiesCache: List<FilterItem>? = null
    private var spheresCache: List<FilterItem>? = null

    override suspend fun getAllCities(): Result<List<FilterItem>> {
        citiesCache?.let { return Result.Success(data = it) }

        return try {
            val response = newsFilterOptionsApi.getAllCities()

            if (response.isSuccessful && response.body() != null) {
                val cities = response.body()!!.map { it.toUiModel() }
                    .sortedBy { it.name }
                citiesCache = cities
                Result.Success(data = cities)
            } else {
                Result.Failure(msg = "some error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Failure(msg = "Нет подключения: ${e.message}")
        }
    }

    override suspend fun getAllSpheres(): Result<List<FilterItem>> {
        spheresCache?.let { return Result.Success(data = it) }
        return try {
            val response = newsFilterOptionsApi.getAllSpheres()
            if (response.isSuccessful && response.body() != null) {
                val resultList = response.body()!!.map { it.toUiModel() }
                spheresCache = resultList
                Result.Success(resultList, "Success")
            } else {
                Result.Failure("some fail: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Failure("Connection issue: ${e.message}")
        }
    }
}