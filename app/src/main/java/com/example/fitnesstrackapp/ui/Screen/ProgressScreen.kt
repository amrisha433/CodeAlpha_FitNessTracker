package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel
import com.example.fitnesstrackapp.ui.components.WeeklyProgressCard
import com.example.fitnesstrackapp.ui.components.WorkoutCategoryChart

@Composable
fun ProgressScreen(
    viewModel: FitnessViewModel
) {

    val workouts by viewModel.workouts.collectAsState()

    val totalWorkouts = workouts.size
    val totalCalories = workouts.sumOf { it.calories }
    val totalDuration = workouts.sumOf { it.duration }
    val categoryData = workouts
        .groupBy { it.category }
        .mapValues { it.value.size }

    val calorieGoal = 2000
    val workoutGoal = 10
    val durationGoal = 500

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Spacer(modifier = Modifier.height(56.dp))

            Text(
                text = "📊 Progress Dashboard",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {

            WeeklyProgressCard(
                totalWorkouts = totalWorkouts,
                totalCalories = totalCalories,
                totalDuration = totalDuration
            )
        }

        item {

            ProgressCard(
                title = "Calories Burned",
                value = "$totalCalories / $calorieGoal kcal",
                progress = (totalCalories.toFloat() / calorieGoal).coerceAtMost(1f),
                color = Color(0xFFEF4444)
            )
        }

        item {

            ProgressCard(
                title = "Workout Minutes",
                value = "$totalDuration / $durationGoal min",
                progress = (totalDuration.toFloat() / durationGoal).coerceAtMost(1f),
                color = Color(0xFF2563EB)
            )
        }

        item {

            ProgressCard(
                title = "Total Workouts",
                value = "$totalWorkouts / $workoutGoal",
                progress = (totalWorkouts.toFloat() / workoutGoal).coerceAtMost(1f),
                color = Color(0xFF16A34A)
            )
        }

        item {

            WorkoutCategoryChart(
                categoryData = categoryData
            )
        }

        item {

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ProgressCard(
    title: String,
    value: String,
    progress: Float,
    color: Color
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = value,
                color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = color
            )
        }
    }
}