package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel

@Composable
fun HomeScreen(viewModel: FitnessViewModel) {

    var showDialog by remember {
        mutableStateOf(false)
    }
    val workouts by viewModel.workouts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {

        Text(
            text = "Fitness Tracker",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showDialog = true
            }
        ) {
            Text("Add Workout")
        }
        Spacer(modifier = Modifier.height(16.dp))

        workouts.forEach { workout ->

            Text(
                text = "${workout.name} • ${workout.duration} min • ${workout.calories} kcal"
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (showDialog) {

        AddWorkoutDialog(
            onDismiss = {
                showDialog = false
            },
            onSave = { name, duration, calories ->

                viewModel.addWorkout(
                    name = name,
                    duration = duration,
                    calories = calories
                )

                showDialog = false
            }
        )
    }

}