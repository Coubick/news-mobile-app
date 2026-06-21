package com.example.news.data.repository

import android.net.Uri
import com.example.news.utils.Result

interface CloudinaryRepository {
    suspend fun uploadImageToCloudinary(file: Uri): Result<String?>

}