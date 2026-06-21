package com.example.news.data.api

import com.example.news.data.dto.CloudinarySignatureResponse
import retrofit2.Response
import retrofit2.http.POST

interface CloudinaryApi {
    //1. метод запроса на серв для получения подписи
    @POST("images/generate-signature")
    suspend fun getSignatureFromServer(): Response<CloudinarySignatureResponse>

}
