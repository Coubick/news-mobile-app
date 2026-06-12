package com.example.news.presentation.screens.news

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NewsListScreen(onNavigateTo: () -> Unit = {}) {
    Text(
        "Main page"
    )
}

@Composable
@Preview
fun Preview(){
    NewsListScreen()
}