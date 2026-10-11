package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DailyGoalsCard(
    totalCalories: Int,
    totalMl: Int,
    totalWorkouts: Int,
    calorieGoal: Int,
    waterGoalMl: Int,
    workoutGoal: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "🎯 Today's Goals",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            GoalRow(
                label    = "🔥 Calories",
                current  = totalCalories,
                goal     = calorieGoal,
                unit     = "kcal"
            )

            Spacer(modifier = Modifier.height(12.dp))

            GoalRow(
                label   = "💧 Water",
                current = totalMl,
                goal    = waterGoalMl,
                unit    = "ml"
            )

            Spacer(modifier = Modifier.height(12.dp))

            GoalRow(
                label   = "🏋️ Workouts",
                current = totalWorkouts,
                goal    = workoutGoal,
                unit    = ""
            )
        }
    }
}

@Composable
private fun GoalRow(label: String, current: Int, goal: Int, unit: String) {
    val progress = if (goal > 0) (current.toFloat() / goal).coerceIn(0f, 1f) else 0f
    val suffix = if (unit.isNotEmpty()) " $unit" else ""

    Text("$label: $current / $goal$suffix")
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth()
    )
}