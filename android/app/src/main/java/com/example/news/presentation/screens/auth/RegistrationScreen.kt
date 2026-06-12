package com.example.news.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.news.R
import com.example.news.presentation.navigation.Screen
import com.example.news.presentation.viewmodel.RegistrationViewModel

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onNavigateTo: (Screen) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isRegistrationSuccessful) {
        if (state.isRegistrationSuccessful) {
            onNavigateTo(Screen.Main)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 40.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.White.copy(alpha = 0.5f),
                    offset = Offset(4f, 4f)
                )
            ),
            modifier = Modifier
                .padding(bottom = 10.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xff55c1f2), Color.Cyan)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .width(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = { viewModel.onUsernameChanged(it) },
            placeholder = { Text(stringResource(R.string.username_register)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            placeholder = { Text(stringResource(R.string.password_register)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChanged(it) },
            placeholder = { Text(stringResource(R.string.confirm_password)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        state.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ElevatedButton(
            onClick = { viewModel.register() },
            enabled = !state.isLoading,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color(0xff55c1f2),
                contentColor = Color.White
            ),

        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(stringResource(R.string.register))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.already_have_acc),
                color = Color.Gray
            )

            Text(
                text = stringResource(R.string.login),
                color = Color.Gray,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable { onNavigateTo(Screen.Login) }
            )
        }
    }
}