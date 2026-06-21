package com.example.news.data.repository

import android.net.Uri
import com.example.news.utils.Result

interface CreateNewsRepository {

    // 1. метод загрузки на cloudinary
    suspend fun uploadImageToCloudinary(file: Uri): Result<String?>

    // 2. метод формирования multipart опционально

}