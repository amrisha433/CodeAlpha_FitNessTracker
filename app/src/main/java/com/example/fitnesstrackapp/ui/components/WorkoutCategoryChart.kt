package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WorkoutCategoryChart(
    categoryData: Map<String, Int>
) {

    val total = categoryData.values.sum()

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Workout Categories",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFB45309)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (categoryData.isEmpty()) {

                Text(
                    text = "No workouts added yet.\nAdd workouts to see category analytics.",
                    color = Color.Gray
                )

            } else {

                categoryData.forEach { (category, count) ->

                    val progress =
                        if (total > 0)
                            count.toFloat() / total
                        else
                            0f

                    Text(
                        text = "$category ($count)"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}