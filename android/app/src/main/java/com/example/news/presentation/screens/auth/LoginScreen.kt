package com.example.news.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.news.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateTo: (Screen) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) {
            onNavigateTo(Screen.Main)
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 100.dp)
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 40.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = remember { TextStyle(
                shadow = Shadow(
                    color = Color.White.copy(alpha = 0.5f),
                    offset = Offset(4f, 4f)
                )
            ) },
            modifier = remember { Modifier
                .padding(bottom = 10.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xff55c1f2), Color.Cyan)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .width(150.dp)
            }
        )

        OutlinedTextField(
            value = state.username,
            onValueChange = { viewModel.onUsernameChanged(it) },
            placeholder = { Text(stringResource(R.string.username_login)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true
        )

        OutlinedTextField(
            value = state.password,
            placeholder = { Text(stringResource(R.string.password_login)) },
            onValueChange = { viewModel.onPasswordChanged(it) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        ElevatedButton(
            onClick = { viewModel.login() },
            enabled = !state.isLoading,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color(0xff55c1f2),
                contentColor = Color.White
            )

        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(stringResource(R.string.login))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.not_have_account),
                color = Color.Gray
            )

            Text(
                text = stringResource(R.string.create_account),
                color = Color.Gray,
                style = remember {TextStyle(textDecoration = TextDecoration.Underline) },
                fontWeight = FontWeight.Bold,

                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable {
                        onNavigateTo(Screen.Registration)
                    }
            )
        }
    }
}