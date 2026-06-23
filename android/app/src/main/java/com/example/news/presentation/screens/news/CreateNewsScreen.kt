package com.example.news.presentation.screens.news

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.news.R
import com.example.news.domain.model.FilterItem
import com.example.news.presentation.viewmodel.CreateNewsUiState
import com.example.news.presentation.viewmodel.NewsFilterOptionsViewModel
import com.example.news.presentation.viewmodel.UploadImageToCloudinaryViewModel
import com.example.news.utils.FilterOptionsType

@Composable
fun CreateNewsScreen(
    viewModel: UploadImageToCloudinaryViewModel = hiltViewModel(),
    filterViewModel: NewsFilterOptionsViewModel = hiltViewModel(),
    onNewsCreated: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val filterState by filterViewModel.uiState.collectAsStateWithLifecycle()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onImageSelected(it) }
    }

    LaunchedEffect(state.isCreated) {
        if (state.isCreated) {
            onNewsCreated()
        }
    }

    CreateNewsContent(
        state = state,
        cities = filterState.cities!!,
        spheres = filterState.spheres!!,
        onTitleChanged = viewModel::onTitleChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onCitySelected = viewModel::onCitySelected,
        onSphereSelected = viewModel::onSphereSelected,
        onImagePickClick = { imagePickerLauncher.launch("image/*") },
        onImageRemove = viewModel::removeImage,
        onSubmit = {
            if (state.selectedImageUri != null && state.uploadedImageUrl == null) {
                viewModel.upload()
            } else {
                viewModel.loadNewsToServer()
            }
        }
    )
}

@Composable
fun CreateNewsContent(
    state: CreateNewsUiState,
    cities: List<FilterItem>,
    spheres: List<FilterItem>,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCitySelected: (Long?) -> Unit,
    onSphereSelected: (Long?) -> Unit,
    onImagePickClick: () -> Unit,
    onImageRemove: () -> Unit,
    onSubmit: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isLoading = state.isUploadingImage || state.isSubmitting

    val canSubmit = !isLoading &&
            state.title.isNotBlank() &&
            state.description.isNotBlank() &&
            state.cityId != null &&
            state.sphereId != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.create_news),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChanged,
            label = { Text(stringResource(R.string.title)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChanged,
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            enabled = !isLoading,
            maxLines = 10
        )

        Text(
            text = stringResource(R.string.city),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        FilterDropdown(
            selectedOptionId = state.cityId,
            options = cities,
            onOptionSelected = onCitySelected,
            modifier = Modifier.fillMaxWidth(),
            optionsType = FilterOptionsType.CITY
        )

        Text(
            text = stringResource(R.string.sphere),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        FilterDropdown(
            selectedOptionId = state.sphereId,
            options = spheres,
            onOptionSelected = onSphereSelected,
            modifier = Modifier.fillMaxWidth(),
            optionsType = FilterOptionsType.SPHERE
        )

        ImagePickerSection(
            selectedImageUri = state.selectedImageUri,
            isUploading = state.isUploadingImage,
            onPickClick = onImagePickClick,
            onRemove = onImageRemove,
            enabled = !isLoading
        )

        state.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = canSubmit
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (state.isUploadingImage) "Загрузка фото..."
                    else "Создание..."
                )
            } else {
                Text("Создать новость")
            }
        }
    }
}

@Composable
fun ImagePickerSection(
    selectedImageUri: Uri?,
    isUploading: Boolean,
    onPickClick: () -> Unit,
    onRemove: () -> Unit,
    enabled: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.load_image),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedImageUri != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedImageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                if (isUploading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Удалить",
                        tint = Color.White
                    )
                }
            }
        } else {
            OutlinedButton(
                onClick = onPickClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp),
                enabled = enabled
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = stringResource(R.string.pick_image), color = Color.Gray)
                }
            }
        }
    }
}