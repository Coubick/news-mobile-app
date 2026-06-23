package com.example.news.presentation.screens.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.news.domain.model.FilterItem
import com.example.news.utils.FilterOptionsType
import com.example.news.R

/**
 *
 * @param selectedOptionId Выбранное значение (null если ничего не выбрано)
 * @param onOptionSelected Callback при выборе опции
 * @param modifier Модификатор
 * @param optionsType - тип оцпий (CITY или SPHERE)
 * @param options Список опций для выбора
 */
@Composable
fun FilterDropdown(
    selectedOptionId: Long?,
    onOptionSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    optionsType: FilterOptionsType,
    options: List<FilterItem>?,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = options!!.find { it.id == selectedOptionId }

    val noFilterText =
        if (optionsType == FilterOptionsType.CITY) stringResource(R.string.city_not_picked)
        else stringResource(R.string.sphere_not_picked)

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(horizontal = 0.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = selectedOption?.name ?: noFilterText,
                color = if (selectedOption != null) Color.Gray else Color.Gray,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 2.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(noFilterText) },
                onClick = {
                    onOptionSelected(null)
                    expanded = false
                }
            )

            HorizontalDivider()

            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.name,
                            color = if (option.id == selectedOptionId)
                                MaterialTheme.colorScheme.primary
                            else Color.Black
                        )
                    },
                    onClick = {
                        onOptionSelected(option.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewDropdownMenu() {
    Row() {
        FilterDropdown(
            selectedOptionId = 1,
            onOptionSelected = {},
            modifier = Modifier,
            optionsType = FilterOptionsType.CITY,
            options = listOf(
                FilterItem(1L, "City1"),
                FilterItem(2L, "Washington")
            )
        )

        VerticalDivider(
            modifier = Modifier
                .height(30.dp)
                .padding(horizontal = 2.dp)
                .align(Alignment.CenterVertically),
            color = Color.Gray
        )

        FilterDropdown(
            selectedOptionId = 2,
            onOptionSelected = {},
            modifier = Modifier,
            optionsType = FilterOptionsType.SPHERE,
            options = listOf(
                FilterItem(1L, "Sport"),
                FilterItem(2L, "Cyber Sport")
            )
        )
    }
}