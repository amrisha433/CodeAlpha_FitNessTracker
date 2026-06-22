package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@Composable
fun ProgressScreen(
    viewModel: FitnessViewModel
) {

    val workouts by viewModel.workouts.collectAsState()

    val totalWorkouts = workouts.size
    val totalCalories = workouts.sumOf { it.calories }
    val totalDuration = workouts.sumOf { it.duration }

    val calorieGoal = 2000
    val workoutGoal = 10
    val durationGoal = 500

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 56.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "📊 Progress Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(modifier = Modifier.height(16.dp))
        WeeklyProgressCard(
            totalWorkouts = totalWorkouts,
            totalCalories = totalCalories,
            totalDuration = totalDuration
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProgressCard(
            title = "Calories Burned",
            value = "$totalCalories / $calorieGoal kcal",
            progress = (totalCalories.toFloat() / calorieGoal).coerceAtMost(1f),
            color = Color(0xFFEF4444)
        )

        ProgressCard(
            title = "Workout Minutes",
            value = "$totalDuration / $durationGoal min",
            progress = (totalDuration.toFloat() / durationGoal).coerceAtMost(1f),
            color = Color(0xFF2563EB)
        )

        ProgressCard(
            title = "Total Workouts",
            value = "$totalWorkouts / $workoutGoal",
            progress = (totalWorkouts.toFloat() / workoutGoal).coerceAtMost(1f),
            color = Color(0xFF16A34A)
        )
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
            containerColor = Color(0xFFEEF2FF)
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
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = value)

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = color
            )
        }
    }
}