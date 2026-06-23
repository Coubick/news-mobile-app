package com.example.news.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.repository.UserRepository
import com.example.news.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val url: String? = null,
    val username: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState = _uiState.asStateFlow()

    init {
        loadCurrentUserData()
    }

    fun loadCurrentUserData() {
        viewModelScope.launch {
            when (val currentUserData = userRepository.getCurrentUserData()) {
                is Result.Success -> {
                    val user = currentUserData.data ?: return@launch
                    _uiState.update {
                        it.copy(
                            url = user.avatarUrl,
                            username = user.username,
                        )
                    }
                }

                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Failure: ${currentUserData.msg}"
                        )
                    }
                }
            }
        }
    }
}