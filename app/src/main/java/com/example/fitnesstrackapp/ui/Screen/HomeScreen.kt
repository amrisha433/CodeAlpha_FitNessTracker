package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel
import com.example.fitnesstrackapp.ui.AddWorkoutDialog
import com.example.fitnesstrackapp.ui.components.DailyGoalsCard
import com.example.fitnesstrackapp.ui.components.StepTrackerCard
import com.example.fitnesstrackapp.ui.components.SummaryCard
import com.example.fitnesstrackapp.ui.components.WaterIntakeCard
import com.example.fitnesstrackapp.ui.components.WorkoutCard

@Composable
fun HomeScreen(viewModel: FitnessViewModel) {

    var showDialog by remember { mutableStateOf(false) }

    // Reactive state from ViewModel
    val goals          by viewModel.goals.collectAsState()
    val todayWorkouts  by viewModel.todayWorkouts.collectAsState()
    val hydrationMl    by viewModel.todayHydrationMl.collectAsState()
    val todaySteps     by viewModel.todaySteps.collectAsState()
    val stepPermission by viewModel.stepPermissionGranted.collectAsState()
    val stepSensor     by viewModel.stepSensorAvailable.collectAsState()

    val totalCaloriesToday = todayWorkouts.sumOf { it.calories }
    val totalWorkoutsToday = todayWorkouts.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        item {
            Text(
                text = "Welcome Back 👋",
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Track your fitness journey today",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth().height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14B8A6)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Workout", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Today's summary using configurable calorie goal
            SummaryCard(
                totalWorkouts = totalWorkoutsToday,
                totalCalories = totalCaloriesToday,
                calorieGoal   = goals.calorieGoal
            )

            // Hydration card – ml-based with quick-add buttons and undo
            WaterIntakeCard(
                totalMl      = hydrationMl,
                goalMl       = goals.waterGoalMl,
                onAddMl      = { ml -> viewModel.addHydration(ml) },
                onUndo       = { viewModel.undoLastHydration() }
            )

            // Step tracker – real sensor or graceful degraded state
            StepTrackerCard(
                steps             = todaySteps,
                goal              = goals.stepGoal,
                permissionGranted = stepPermission,
                sensorAvailable   = stepSensor
            )

            // Daily goals overview using configurable targets
            DailyGoalsCard(
                totalCalories  = totalCaloriesToday,
                totalMl        = hydrationMl,
                totalWorkouts  = totalWorkoutsToday,
                calorieGoal    = goals.calorieGoal,
                waterGoalMl    = goals.waterGoalMl,
                workoutGoal    = goals.workoutCountGoal
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Today's Workouts", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (todayWorkouts.isEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "💪 No workouts yet today",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Tap Add Workout to start your fitness journey!",
                    color = Color(0xFF94A3B8)
                )
            }
        } else {
            items(todayWorkouts) { workout ->
                WorkoutCard(
                    workout  = workout,
                    onDelete = { viewModel.deleteWorkout(workout) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (showDialog) {
        AddWorkoutDialog(
            onDismiss = { showDialog = false },
            onSave = { name, category, duration, calories ->
                viewModel.addWorkout(
                    name     = name,
                    category = category,
                    duration = duration,
                    calories = calories
                )
                showDialog = false
            }
        )
    }
}