package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.ui.theme.Teal

@Composable
fun SummaryCard(
    totalWorkouts: Int,
    totalCalories: Int,
    calorieGoal: Int = 500   // default matches GoalPreferences.DEFAULT_CALORIES
) {
    val safeGoal = calorieGoal.coerceAtLeast(1)
    val progress = (totalCalories.toFloat() / safeGoal).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "📊 Today's Summary",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🏋️ Workouts today: $totalWorkouts",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFCBD5E1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🔥 Recorded calories: $totalCalories kcal",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF22C55E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Daily calorie goal: $totalCalories / $safeGoal kcal",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = Teal,
                trackColor = Color.Gray.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${(progress * 100).toInt()}% of goal",
                color = Color(0xFF94A3B8),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}