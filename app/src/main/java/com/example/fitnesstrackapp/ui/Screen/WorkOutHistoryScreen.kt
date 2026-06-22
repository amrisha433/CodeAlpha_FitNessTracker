package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class HistoryWorkout(
    val name: String,
    val duration: Int,
    val calories: Int,
    val date: String
)

@Composable
fun WorkoutHistoryScreen() {

    val history = listOf(
        HistoryWorkout("Running", 40, 2500, "22 Jun"),
        HistoryWorkout("Yoga", 45, 2000, "21 Jun"),
        HistoryWorkout("Walking", 70, 3000, "20 Jun"),
        HistoryWorkout("Cycling", 35, 2800, "19 Jun")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 46.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = "🏋️ Workout History",
            color = Color.Black,
            style = MaterialTheme.typography.headlineMedium,

        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {

            items(history) { workout ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEEF2FF)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = workout.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("⏱ ${workout.duration} min")
                        Text("🔥 ${workout.calories} kcal")
                        Text("📅 ${workout.date}")
                    }
                }
            }
        }
    }
}