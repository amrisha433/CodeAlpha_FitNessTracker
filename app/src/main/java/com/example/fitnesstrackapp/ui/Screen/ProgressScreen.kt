package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.Data.Workout
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel
import com.example.fitnesstrackapp.ui.components.WeeklyProgressCard
import com.example.fitnesstrackapp.ui.components.WorkoutCategoryChart
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ProgressScreen(viewModel: FitnessViewModel) {

    val weeklyWorkouts by viewModel.weeklyWorkouts.collectAsState()
    val monthlyWorkouts by viewModel.monthlyWorkouts.collectAsState()
    val allWorkouts by viewModel.workouts.collectAsState()
    val currentStreak by viewModel.currentStreak.collectAsState()

    // 0 = Week, 1 = Month
    var selectedPeriod by remember { mutableIntStateOf(0) }

    val periodLabels = listOf("This Week", "This Month")

    val activeWorkouts =
        if (selectedPeriod == 0) weeklyWorkouts else monthlyWorkouts

    val totalWorkouts = activeWorkouts.size
    val totalCalories = activeWorkouts.sumOf { it.calories }
    val totalDuration = activeWorkouts.sumOf { it.duration }

    val categoryData = activeWorkouts
        .groupBy { it.category }
        .mapValues { it.value.size }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = 56.dp,
            bottom = 100.dp
        )
    ) {

        // Dashboard heading
        item {
            Column {
                Text(
                    text = "📊 Progress Dashboard",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "All-time workouts: ${allWorkouts.size} • Current streak: $currentStreak days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Week / Month selector
        item {
            TabRow(selectedTabIndex = selectedPeriod) {
                periodLabels.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedPeriod == index,
                        onClick = { selectedPeriod = index },
                        text = { Text(label) }
                    )
                }
            }
        }

        // Period summary: no goal ranges or progress bars
        item {
            WeeklyProgressCard(
                label = if (selectedPeriod == 0) {
                    "📅 This Week"
                } else {
                    "📅 This Month"
                },
                totalWorkouts = totalWorkouts,
                totalCalories = totalCalories,
                totalDurationMin = totalDuration
            )
        }

        // Workout category distribution
        item {
            WorkoutCategoryChart(categoryData = categoryData)
        }

        // Workout log
        if (activeWorkouts.isNotEmpty()) {

            item {
                Text(
                    text = "Workout Log",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            val grouped = activeWorkouts
                .groupBy { workout ->
                    Instant.ofEpochMilli(workout.date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                .toSortedMap(compareByDescending { it })

            grouped.forEach { (date, dayWorkouts) ->

                item(key = date.toString()) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = date.format(
                                DateTimeFormatter.ofPattern("EEE, dd MMM")
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        dayWorkouts.forEach { workout ->
                            WorkoutLogRow(workout)
                        }

                        HorizontalDivider()
                    }
                }
            }

        } else {

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = if (selectedPeriod == 0) {
                            "No workouts recorded this week.\nAdd a workout on the Home screen!"
                        } else {
                            "No workouts recorded this month yet."
                        },
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkoutLogRow(workout: Workout) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "${workout.category} · ${workout.duration} min · ${workout.calories} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}