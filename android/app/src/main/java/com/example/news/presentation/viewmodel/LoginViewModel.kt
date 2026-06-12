package com.example.news.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.news.utils.Result
import kotlinx.coroutines.launch

data class LoginUiState (
    val username: String="",
    val password: String="",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false

)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChanged(username: String) {
        _uiState.update { it.copy(username = username, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun login() {
        val state = _uiState.value
        val validationError = validate(state)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val result = authRepository.login(state.username, state.password)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
                }

                is Result.Failure -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.msg) }
                }
            }
        }
    }

    private fun validate(state: LoginUiState): String? {
        if (state.username.length < 2) return "Name's min length is 2"
        if (state.username.length > 50) return "Name's max length is 50"
        if (state.password.length < 6) return "Password's min length is 6"
        return null
    }
}