package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel

@Composable
fun ProfileScreen(
    viewModel: FitnessViewModel
) {

    val workouts by viewModel.workouts.collectAsState()

    val totalWorkouts = workouts.size
    val totalCalories = workouts.sumOf { it.calories }
    val totalDuration = workouts.sumOf { it.duration }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "👤",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Fitness Dashboard",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Track • Improve • Achieve",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileInfoCard(
            title = "🎯 Fitness Goal",
            value = "Stay Active & Healthy"
        )

        ProfileInfoCard(
            title = "🏋️ Total Workouts",
            value = totalWorkouts.toString()
        )

        ProfileInfoCard(
            title = "🔥 Total Calories Burned",
            value = "$totalCalories kcal"
        )

        ProfileInfoCard(
            title = "⏱ Total Workout Time",
            value = "$totalDuration min"
        )

        ProfileInfoCard(
            title = "💧 Water Goal",
            value = "8 Glasses / Day"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Fitness Tracker v1.0",
            color = Color.Gray
        )
    }
}

@Composable
fun ProfileInfoCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value
            )
        }
    }
}