package com.example.news.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.repository.AuthRepository
import com.example.news.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistrationSuccessful: Boolean = false
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onUsernameChanged(username: String) {
        _uiState.update { it.copy(username = username, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onConfirmPasswordChanged(password: String) {
        _uiState.update { it.copy(confirmPassword = password, errorMessage = null) }
    }

    fun register() {
        val state = _uiState.value
        val validationError = validate(state)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            authRepository.register(uiState.value.username, uiState.value.password)

            when (val result = authRepository.register(state.username, state.password)) {
                is Result.Success<*> -> {
                    _uiState.update { it.copy(isLoading = false, isRegistrationSuccessful = true) }
                }

                is Result.Failure<*> -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.msg) }
                }
            }
        }
    }

    private fun validate(state: RegistrationUiState): String? {
        if (state.username.length < 2) return "Name's min length is 2"
        if (state.username.length > 50) return "Name's max length is 50"
        if (state.password.length < 6) return "Password's min length is 6"
        if (state.password != state.confirmPassword) return "Passwords are different"
        return null
    }
}