package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    val goals           by viewModel.goals.collectAsState()
    val weeklyWorkouts  by viewModel.weeklyWorkouts.collectAsState()
    val monthlyWorkouts by viewModel.monthlyWorkouts.collectAsState()
    val allWorkouts     by viewModel.workouts.collectAsState()
    val currentStreak   by viewModel.currentStreak.collectAsState()

    // Period selector
    var selectedPeriod by remember { mutableIntStateOf(0) }  // 0 = Week, 1 = Month
    val periodLabels = listOf("This Week", "This Month")

    val activeWorkouts = if (selectedPeriod == 0) weeklyWorkouts else monthlyWorkouts

    val totalWorkouts  = activeWorkouts.size
    val totalCalories  = activeWorkouts.sumOf { it.calories }
    val totalDuration  = activeWorkouts.sumOf { it.duration }
    val categoryData   = activeWorkouts.groupBy { it.category }.mapValues { it.value.size }

    // Goal progress for selected period
    val calorieProgress  = if (goals.calorieGoal > 0)  (totalCalories.toFloat() / goals.calorieGoal).coerceAtMost(1f)  else 0f
    val durationProgress = if (goals.workoutDurationGoalMin > 0) (totalDuration.toFloat() / goals.workoutDurationGoalMin).coerceAtMost(1f) else 0f
    val workoutProgress  = if (goals.workoutCountGoal > 0) (totalWorkouts.toFloat() / goals.workoutCountGoal).coerceAtMost(1f) else 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 56.dp, bottom = 100.dp)
    ) {
        item {
            Text("📊 Progress Dashboard", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "All-time workouts: ${allWorkouts.size}  •  Current streak: $currentStreak days",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Period selector tabs
        item {
            TabRow(selectedTabIndex = selectedPeriod) {
                periodLabels.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedPeriod == index,
                        onClick  = { selectedPeriod = index },
                        text     = { Text(label) }
                    )
                }
            }
        }

        // Summary card
        item {
            WeeklyProgressCard(
                label             = if (selectedPeriod == 0) "📅 This Week" else "📅 This Month",
                totalWorkouts     = totalWorkouts,
                totalCalories     = totalCalories,
                totalDurationMin  = totalDuration
            )
        }

        // Goal progress cards
        item {
            ProgressCard(
                title    = "Recorded Calories",
                value    = "$totalCalories / ${goals.calorieGoal} kcal",
                progress = calorieProgress,
                color    = Color(0xFFEF4444)
            )
        }

        item {
            ProgressCard(
                title    = "Workout Duration",
                value    = "$totalDuration / ${goals.workoutDurationGoalMin} min",
                progress = durationProgress,
                color    = Color(0xFF2563EB)
            )
        }

        item {
            ProgressCard(
                title    = "Workout Count",
                value    = "$totalWorkouts / ${goals.workoutCountGoal}",
                progress = workoutProgress,
                color    = Color(0xFF16A34A)
            )
        }

        // Category distribution
        item {
            WorkoutCategoryChart(categoryData = categoryData)
        }

        // Daily breakdown list (last 7 or 30 days)
        if (activeWorkouts.isNotEmpty()) {
            item {
                Text(
                    "Workout Log",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Group by date
            val grouped = activeWorkouts
                .groupBy { workout ->
                    Instant.ofEpochMilli(workout.date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                .entries
                .sortedByDescending { it.key }

            grouped.forEach { (date, dayWorkouts) ->
                item(key = date.toString()) {
                    val dateStr = date.format(DateTimeFormatter.ofPattern("EEE, dd MMM"))
                    Text(
                        dateStr,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    dayWorkouts.forEach { w ->
                        WorkoutLogRow(w)
                        Spacer(Modifier.height(4.dp))
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
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
                        text = if (selectedPeriod == 0)
                            "No workouts recorded this week.\nAdd a workout on the Home screen!"
                        else
                            "No workouts recorded this month yet.",
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
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(workout.name, style = MaterialTheme.typography.bodyMedium)
                Text(
                    "${workout.category} · ${workout.duration} min · ${workout.calories} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
            .padding(bottom = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}