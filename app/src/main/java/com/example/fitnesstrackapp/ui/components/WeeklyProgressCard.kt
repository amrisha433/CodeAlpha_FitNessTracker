package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WeeklyProgressCard(
    totalWorkouts: Int,
    totalCalories: Int,
    totalDuration: Int
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFBEB)
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "📅 Weekly Summary",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFB45309)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🏋️ Total Workouts: $totalWorkouts",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🔥 Calories Burned: $totalCalories kcal",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⏱ Workout Duration: $totalDuration min",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}