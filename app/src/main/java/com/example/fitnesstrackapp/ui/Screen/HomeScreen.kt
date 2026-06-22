package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel
import com.example.fitnesstrackapp.ui.components.DailyGoalsCard
import com.example.fitnesstrackapp.ui.components.StepTrackerCard
import com.example.fitnesstrackapp.ui.components.SummaryCard
import com.example.fitnesstrackapp.ui.components.WaterIntakeCard
import com.example.fitnesstrackapp.ui.components.WeeklyProgressCard
import com.example.fitnesstrackapp.ui.components.WorkoutCard
import com.example.fitnesstrackapp.ui.theme.Teal

@Composable
fun HomeScreen(viewModel: FitnessViewModel) {

    var waterIntake by remember {
        mutableIntStateOf(0)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }
    var steps by remember {
        mutableIntStateOf(0)
    }

    val workouts by viewModel.workouts.collectAsState()

    val totalWorkouts = workouts.size
    val totalCalories = workouts.sumOf { it.calories }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = 16.dp,
            bottom = 100.dp
        )
    ) {

        item {
            Text(
                text = "Welcome Back 👋",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Track your fitness journey today",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF14B8A6)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Add Workout",
                    fontSize = 18.sp,
                )
            }

            SummaryCard(
                totalWorkouts = totalWorkouts,
                totalCalories = totalCalories
            )

            WaterIntakeCard(
                waterIntake = waterIntake,
                onAddGlass = {
                    if (waterIntake < 8) {
                        waterIntake++
                    }
                }
            )
            StepTrackerCard(
                steps = steps,
                onAddSteps = {
                    if (steps < 10000) {
                        steps += 500
                    }
                }
            )

            DailyGoalsCard(
                totalCalories = totalCalories,
                waterIntake = waterIntake,
                totalWorkouts = totalWorkouts
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Workouts",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (workouts.isEmpty()) {

            item {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "💪 No workouts yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Tap Add Workout to start your fitness journey!",
                    color = Color(0xFF94A3B8)
                )
            }

        } else {

            items(workouts) { workout ->

                WorkoutCard(
                    workout = workout,
                    onDelete = {
                        viewModel.deleteWorkout(workout)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
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