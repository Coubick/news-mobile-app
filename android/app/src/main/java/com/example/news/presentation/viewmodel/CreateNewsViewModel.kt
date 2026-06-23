package com.example.news.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.news.domain.repository.CloudinaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.news.domain.repository.NewsRepository
import com.example.news.utils.Result
import kotlinx.coroutines.launch

data class CreateNewsUiState(
    val cityId: Long? = null,
    val sphereId: Long? = null,
    val title: String = "",
    val description: String = "",
    val selectedImageUri: Uri? = null,
    val uploadedImageUrl: String? = null,
    val errorMessage: String? = null,
    val isUploadingImage: Boolean = false,
    val isCreated: Boolean = false,
    val isSubmitting: Boolean = false,
)

@HiltViewModel
class UploadImageToCloudinaryViewModel @Inject constructor(
    private val cloudinaryRepository: CloudinaryRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateNewsUiState())

    val uiState: StateFlow<CreateNewsUiState> = _uiState.asStateFlow()

    fun onTitleChanged(title: String) {
        _uiState.update { it.copy(title = title, errorMessage = null) }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description, errorMessage = null) }
    }

    fun onCitySelected(cityId: Long?) {
        _uiState.update { it.copy(cityId = cityId) }
    }

    fun onSphereSelected(sphereId: Long?) {
        _uiState.update { it.copy(sphereId = sphereId) }
    }

    fun upload() {
        val uri: Uri = _uiState.value.selectedImageUri ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUploadingImage = true
                )
            }

            when (val secureUrl: Result<String?> =
                cloudinaryRepository.uploadImageToCloudinary(
                    file = uri
                )) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isUploadingImage = false,
                            uploadedImageUrl = secureUrl.data
                        )
                    }
                    loadNewsToServer()
                }

                is Result.Failure -> {

                    _uiState.update {
                        it.copy(
                            isUploadingImage = false,
                            errorMessage = secureUrl.msg
                        )
                    }
                }
            }
        }
    }

    fun loadNewsToServer() {
        viewModelScope.launch {
            val result = newsRepository.createNews(
                title = _uiState.value.title,
                description = _uiState.value.description,
                cityId = _uiState.value.cityId,
                sphereId = _uiState.value.sphereId,
                imageUrl = _uiState.value.uploadedImageUrl
            )

            when (result) {
                is Result.Success -> {
                    _uiState.update { it.copy(isCreated = true) }
                }

                is Result.Failure -> {
                    _uiState.update { it.copy(errorMessage = result.msg) }
                }
            }
        }
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update {
            it.copy(
                selectedImageUri = uri,
                uploadedImageUrl = null
            )
        }
    }

    fun removeImage() {
        _uiState.update {
            it.copy(
                selectedImageUri = null,
                uploadedImageUrl = null
            )
        }
    }
}
