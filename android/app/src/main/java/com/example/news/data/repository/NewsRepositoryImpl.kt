package com.example.news.data.repository

import com.example.news.data.api.NewsApi
import com.example.news.data.dto.CreateNewsRequest
import com.example.news.data.dto.NewsDto
import com.example.news.domain.repository.NewsRepository
import com.example.news.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsRepository {
    override suspend fun getNewsList(
        cityId: Long?,
        sphereId: Long?,
        limit: Int,
        offset: Int
    ): Result<List<NewsDto>> {
        return try {
            val response = newsApi.getNewsList(cityId, sphereId, limit, offset)

            if (response.isSuccessful && response.body() != null) {
                val resultList = response.body()!!.items
                Result.Success(resultList, "Success")
            } else {
                Result.Failure(msg = "Failure: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Failure("${e.message}")
        }
    }

    override suspend fun createNews(
        title: String,
        description: String,
        cityId: Long?,
        sphereId: Long?,
        imageUrl: String?
    ): Result<Unit> {
        return try {

            val request = CreateNewsRequest(
                title = title,
                description = description,
                cityId = cityId,
                sphereId = sphereId,
                imageUrl = imageUrl
            )

            val response = newsApi.createNews(request)
            if (response.isSuccessful) {
                Result.Success(
                    msg = "Success",
                    data = Unit
                )

            } else {
                Result.Failure(msg = "Error ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Failure("Exception: ${e.message}")
        }
    }
}