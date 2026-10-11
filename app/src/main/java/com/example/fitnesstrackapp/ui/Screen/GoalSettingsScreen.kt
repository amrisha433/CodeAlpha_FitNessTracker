package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.Preferences.GoalPreferences
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSettingsScreen(viewModel: FitnessViewModel) {

    val goals by viewModel.goals.collectAsState()

    var stepGoalText          by remember(goals.stepGoal)           { mutableStateOf(goals.stepGoal.toString()) }
    var waterGoalText         by remember(goals.waterGoalMl)        { mutableStateOf(goals.waterGoalMl.toString()) }
    var durationGoalText      by remember(goals.workoutDurationGoalMin) { mutableStateOf(goals.workoutDurationGoalMin.toString()) }
    var workoutCountGoalText  by remember(goals.workoutCountGoal)   { mutableStateOf(goals.workoutCountGoal.toString()) }
    var calorieGoalText       by remember(goals.calorieGoal)        { mutableStateOf(goals.calorieGoal.toString()) }

    var showSavedSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSavedSnackbar) {
        if (showSavedSnackbar) {
            snackbarHostState.showSnackbar("Goals saved ✓")
            showSavedSnackbar = false
        }
    }

    fun validate(): String? {
        val steps  = stepGoalText.toIntOrNull()
        val water  = waterGoalText.toIntOrNull()
        val dur    = durationGoalText.toIntOrNull()
        val count  = workoutCountGoalText.toIntOrNull()
        val cal    = calorieGoalText.toIntOrNull()

        if (steps == null || steps < GoalPreferences.MIN_STEPS || steps > GoalPreferences.MAX_STEPS)
            return "Steps must be between ${GoalPreferences.MIN_STEPS} and ${GoalPreferences.MAX_STEPS}"
        if (water == null || water < GoalPreferences.MIN_WATER || water > GoalPreferences.MAX_WATER)
            return "Water goal must be between ${GoalPreferences.MIN_WATER} and ${GoalPreferences.MAX_WATER} ml"
        if (dur == null || dur < GoalPreferences.MIN_DURATION || dur > GoalPreferences.MAX_DURATION)
            return "Duration must be between ${GoalPreferences.MIN_DURATION} and ${GoalPreferences.MAX_DURATION} min"
        if (count == null || count < GoalPreferences.MIN_WORKOUT_COUNT || count > GoalPreferences.MAX_WORKOUT_COUNT)
            return "Workout count must be between ${GoalPreferences.MIN_WORKOUT_COUNT} and ${GoalPreferences.MAX_WORKOUT_COUNT}"
        if (cal == null || cal < GoalPreferences.MIN_CALORIES || cal > GoalPreferences.MAX_CALORIES)
            return "Calorie goal must be between ${GoalPreferences.MIN_CALORIES} and ${GoalPreferences.MAX_CALORIES} kcal"
        return null
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text("🎯 Daily Goal Settings", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Set targets that will be used across Home, Progress and all progress indicators.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            GoalTextField(
                label = "👣 Daily Step Goal",
                value = stepGoalText,
                hint = "${GoalPreferences.MIN_STEPS} – ${GoalPreferences.MAX_STEPS}",
                onValueChange = { stepGoalText = it }
            )
            GoalTextField(
                label = "💧 Daily Water Goal (ml)",
                value = waterGoalText,
                hint = "${GoalPreferences.MIN_WATER} – ${GoalPreferences.MAX_WATER} ml",
                onValueChange = { waterGoalText = it }
            )
            GoalTextField(
                label = "⏱ Daily Workout Duration (min)",
                value = durationGoalText,
                hint = "${GoalPreferences.MIN_DURATION} – ${GoalPreferences.MAX_DURATION} min",
                onValueChange = { durationGoalText = it }
            )
            GoalTextField(
                label = "🏋️ Daily Workout Count",
                value = workoutCountGoalText,
                hint = "${GoalPreferences.MIN_WORKOUT_COUNT} – ${GoalPreferences.MAX_WORKOUT_COUNT}",
                onValueChange = { workoutCountGoalText = it }
            )
            GoalTextField(
                label = "🔥 Daily Calorie Goal (kcal)",
                value = calorieGoalText,
                hint = "${GoalPreferences.MIN_CALORIES} – ${GoalPreferences.MAX_CALORIES} kcal",
                onValueChange = { calorieGoalText = it }
            )

            var errorMessage by remember { mutableStateOf<String?>(null) }

            errorMessage?.let {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Button(
                onClick = {
                    errorMessage = validate()
                    if (errorMessage == null) {
                        viewModel.saveGoals(
                            GoalPreferences.Goals(
                                stepGoal               = stepGoalText.toInt(),
                                waterGoalMl            = waterGoalText.toInt(),
                                workoutDurationGoalMin = durationGoalText.toInt(),
                                workoutCountGoal       = workoutCountGoalText.toInt(),
                                calorieGoal            = calorieGoalText.toInt()
                            )
                        )
                        showSavedSnackbar = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Goals")
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun GoalTextField(
    label: String,
    value: String,
    hint: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.filter { c -> c.isDigit() }) },
        label = { Text(label) },
        supportingText = { Text(hint, style = MaterialTheme.typography.bodySmall) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}
