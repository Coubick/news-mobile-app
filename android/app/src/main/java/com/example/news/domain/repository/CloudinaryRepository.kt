package com.example.news.domain.repository

import android.net.Uri
import com.example.news.utils.Result

interface CloudinaryRepository {
    suspend fun uploadImageToCloudinary(file: Uri): Result<String?>

}