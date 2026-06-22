package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.ui.theme.Teal

@Composable
fun SummaryCard(
    totalWorkouts: Int,
    totalCalories: Int,
    calorieGoal: Int = 2000
) {

    val progress =
        (totalCalories.toFloat() / calorieGoal)
            .coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "📊 Today's Summary",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🏋️ Workouts: $totalWorkouts",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFCBD5E1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🔥 Calories Burned: $totalCalories kcal",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF22C55E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Goal: $totalCalories / $calorieGoal kcal",
                color = Color.White
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
                text = "${(progress * 100).toInt()}% Completed",
                color = Color(0xFF94A3B8)
            )
        }
    }
}