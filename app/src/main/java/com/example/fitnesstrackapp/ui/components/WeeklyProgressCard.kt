package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Displays workout statistics for a bounded time period (week or month).
 *
 * All values are passed in from the ViewModel and are correctly date-scoped –
 * this card no longer shows all-time totals labelled as "weekly".
 */
@Composable
fun WeeklyProgressCard(
    label: String = "📅 This Week",
    totalWorkouts: Int,
    totalCalories: Int,
    totalDurationMin: Int,
    avgDailySteps: Int = -1   // -1 = not available
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFB45309)
            )

            Spacer(modifier = Modifier.height(16.dp))

            StatRow("🏋️ Workouts", "$totalWorkouts")
            StatRow("🔥 Recorded Calories", "$totalCalories kcal")
            StatRow("⏱ Total Duration", "$totalDurationMin min")

            if (avgDailySteps >= 0) {
                StatRow("👣 Avg Daily Steps", "$avgDailySteps steps")
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary)
    }
}