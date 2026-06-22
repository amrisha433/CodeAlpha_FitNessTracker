package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DailyGoalsCard(
    totalCalories: Int,
    waterIntake: Int,
    totalWorkouts: Int
) {

    val calorieGoal = 2000
    val waterGoal = 8
    val workoutGoal = 3

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEEF2FF)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "🎯 Daily Goals",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("🔥 Calories: $totalCalories / $calorieGoal")

            LinearProgressIndicator(
                progress = {
                    (totalCalories.toFloat() / calorieGoal)
                        .coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("💧 Water: $waterIntake / $waterGoal")

            LinearProgressIndicator(
                progress = {
                    (waterIntake.toFloat() / waterGoal)
                        .coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("🏋️ Workouts: $totalWorkouts / $workoutGoal")

            LinearProgressIndicator(
                progress = {
                    (totalWorkouts.toFloat() / workoutGoal)
                        .coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}