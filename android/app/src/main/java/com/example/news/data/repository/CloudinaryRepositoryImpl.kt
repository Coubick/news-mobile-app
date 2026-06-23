package com.example.news.data.repository

import android.net.Uri
import com.cloudinary.Cloudinary
import com.example.news.data.api.CloudinaryApi
import com.example.news.data.dto.CloudinarySignatureResponse
import com.example.news.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import com.example.news.domain.repository.CloudinaryRepository
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class CloudinaryRepositoryImpl @Inject constructor(
    private val cloudinary: Cloudinary,
    private val cloudinaryApi: CloudinaryApi,
    @ApplicationContext private val context: Context
) : CloudinaryRepository {


    override suspend fun uploadImageToCloudinary(uri: Uri): Result<String?> {
        return try {
            val signatureResponse: Response<CloudinarySignatureResponse> =
                cloudinaryApi.getSignatureFromServer()
            if (!signatureResponse.isSuccessful || signatureResponse.body() == null) {
                return Result.Failure(msg = "failed to get signature: ${signatureResponse.code()}")
            }


            val options: HashMap<String, String> = hashMapOf(
                "signature" to signatureResponse.body()!!.signature,
                "timestamp" to signatureResponse.body()!!.timestamp.toString(),
                "folder" to signatureResponse.body()!!.folder,
                "api_key" to signatureResponse.body()!!.apiKey,
                "cloud_name" to signatureResponse.body()!!.cloudName
            )

            val file = uriToFile(uri) ?: return Result.Failure(msg = "failed to read file")

            val response: Map<*, *> = withContext(Dispatchers.IO) {
                cloudinary.uploader().upload(file, options)
            }

            val secureUrl = response["secure_url"] as? String
                ?: return Result.Failure(msg = "url didn't get")

            Result.Success(data = secureUrl, msg = "Success")

        } catch (e: Exception) {
            Result.Failure("Exception: ${e.message}")
        }
    }

    private fun uriToFile(uri: Uri): java.io.File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = java.io.File.createTempFile("upload_", ".jpg", context.cacheDir)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            inputStream.close()
            tempFile
        } catch (e: Exception) {
            null
        }
    }

}