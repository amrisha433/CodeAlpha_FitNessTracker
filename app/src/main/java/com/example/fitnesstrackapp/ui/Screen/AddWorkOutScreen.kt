package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel

@Composable
fun AddWorkoutScreen(
    viewModel: FitnessViewModel
) {

    var workoutName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Add Workout",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = workoutName,
            onValueChange = { workoutName = it },
            label = { Text("Workout Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (Minutes)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories Burned") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Save workout later
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Workout")
        }
    }
}