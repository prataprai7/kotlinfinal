package com.example.projectk.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectk.R

@Composable
fun FiltersRow(filters: List<String>) {
    var selectedFilter by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(filters) { filter ->
                val isSelected = selectedFilter == filter

                FilterChip(
                    onClick = {
                        selectedFilter = if (isSelected) null else filter
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (isSelected) {
                            colorResource(R.color.teal_200)
                        } else {
                            Color.White
                        },
                        labelColor = if (isSelected) {
                            Color.White
                        } else {
                            Color.DarkGray
                        },
                        iconColor = if (isSelected) {
                            Color.White
                        } else {
                            Color.DarkGray
                        }
                    ),
                    selected = isSelected,
                    leadingIcon = {
                        when (filter) {
                            "Filter" -> Icon(
                                painter = painterResource(id = R.drawable.dining),
                                contentDescription = "Filter",
                                modifier = Modifier.size(16.dp)
                            )
                            "Flash Sale" -> Icon(
                                painter = painterResource(id = R.drawable.snack_meal),
                                contentDescription = "Flash Sale",
                                modifier = Modifier.size(16.dp)
                            )
                            "Under 30 mins" -> Icon(
                                painter = painterResource(id = R.drawable.timer),
                                contentDescription = "Under 30 mins",
                                modifier = Modifier.size(16.dp)
                            )
                            "Rating" -> Icon(
                                painter = painterResource(id = R.drawable.rating),
                                contentDescription = "Rating",
                                modifier = Modifier.size(16.dp)
                            )
                            "Schedule" -> Icon(
                                painter = painterResource(id = R.drawable.notes),
                                contentDescription = "Schedule",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    trailingIcon = {
                        if (isSelected) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "Clear filter",
                                modifier = Modifier.size(16.dp)
                            )
                        } else if (filter == "Filter") {
                            Icon(
                                painter = painterResource(id = R.drawable.arrowdown),
                                contentDescription = "Filter options",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    },
                    border = BorderStroke(
                        width = 0.4.dp,
                        color = colorResource(R.color.teal_200)
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}