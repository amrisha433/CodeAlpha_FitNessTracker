package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.Preferences.ReminderPreferences
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel

@Composable
fun ProfileScreen(viewModel: FitnessViewModel) {

    val workouts         by viewModel.workouts.collectAsState()
    val goals            by viewModel.goals.collectAsState()
    val reminderSettings by viewModel.reminderSettings.collectAsState()
    val achievements     by viewModel.achievements.collectAsState()
    val currentStreak    by viewModel.currentStreak.collectAsState()

    val totalWorkouts  = workouts.size
    val totalCalories  = workouts.sumOf { it.calories }
    val totalDuration  = workouts.sumOf { it.duration }
    val unlockedCount  = achievements.count { it.isUnlocked }

    // Sub-screen navigation within Profile
    var subScreen by remember { mutableStateOf<String?>(null) }

    when (subScreen) {
        "goals"        -> GoalSettingsScreen(viewModel = viewModel)
        "achievements" -> AchievementsScreen(viewModel = viewModel)
        "reminders"    -> ReminderSettingsSection(
            settings  = reminderSettings,
            onSave    = { viewModel.saveReminderSettings(it) },
            onBack    = { subScreen = null }
        )
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(26.dp))

                Text("👤", style = MaterialTheme.typography.displayLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Fitness Dashboard", style = MaterialTheme.typography.headlineSmall)
                Text("Track • Improve • Achieve", color = Color.Gray)

                Spacer(modifier = Modifier.height(24.dp))

                // Stats cards
                ProfileInfoCard("🏋️ Total Workouts",    totalWorkouts.toString())
                ProfileInfoCard("🔥 Total Recorded Cal", "$totalCalories kcal")
                ProfileInfoCard("⏱ Total Workout Time",  "$totalDuration min")
                ProfileInfoCard("🔥 Current Streak",     "$currentStreak days")
                ProfileInfoCard("🏆 Achievements",       "$unlockedCount / ${achievements.size} unlocked")

                Spacer(modifier = Modifier.height(24.dp))

                // Navigation to sub-sections
                Text(
                    "Settings",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )

                ProfileNavCard(
                    title    = "🎯 Customize Daily Goals",
                    subtitle = "Steps, water, calories, workouts",
                    icon     = Icons.Default.Settings,
                    onClick  = { subScreen = "goals" }
                )

                ProfileNavCard(
                    title    = "🏆 Achievements",
                    subtitle = "$unlockedCount unlocked · $currentStreak day streak",
                    icon     = Icons.Default.EmojiEvents,
                    onClick  = { subScreen = "achievements" }
                )

                ProfileNavCard(
                    title    = "💧 Hydration Reminders",
                    subtitle = if (reminderSettings.enabled)
                        "Every ${reminderSettings.intervalHours}h · ${reminderSettings.windowStartHour}:00–${reminderSettings.windowEndHour}:00"
                    else "Disabled",
                    icon     = Icons.Default.Settings,
                    onClick  = { subScreen = "reminders" }
                )

                Spacer(modifier = Modifier.height(24.dp))
                Text("Fitness Tracker v1.0", color = Color.Gray)
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ProfileInfoCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(value, style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun ProfileNavCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ── Reminder Settings Sub-section ─────────────────────────────────────────────

@Composable
private fun ReminderSettingsSection(
    settings: ReminderPreferences.ReminderSettings,
    onSave: (ReminderPreferences.ReminderSettings) -> Unit,
    onBack: () -> Unit
) {
    var enabled    by remember { mutableStateOf(settings.enabled) }
    var interval   by remember { mutableStateOf(settings.intervalHours.toString()) }
    var winStart   by remember { mutableStateOf(settings.windowStartHour.toString()) }
    var winEnd     by remember { mutableStateOf(settings.windowEndHour.toString()) }
    var showSaved  by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(showSaved) {
        if (showSaved) {
            snackbarHostState.showSnackbar("Reminder settings saved")
            showSaved = false
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) { Text("← Back") }
                Spacer(Modifier.width(8.dp))
                Text("💧 Hydration Reminders", style = MaterialTheme.typography.headlineSmall)
            }

            Text(
                "WorkManager delivers reminders periodically. Exact timing may vary by up to ~15 minutes " +
                "depending on device battery optimisations.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Reminders", style = MaterialTheme.typography.titleMedium)
                Switch(checked = enabled, onCheckedChange = { enabled = it })
            }

            AnimatedVisibility(visible = enabled) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = interval,
                        onValueChange = { interval = it.filter { c -> c.isDigit() } },
                        label = { Text("Reminder Interval (hours)") },
                        supportingText = { Text("Minimum 1 hour") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = winStart,
                        onValueChange = { winStart = it.filter { c -> c.isDigit() } },
                        label = { Text("Start Hour (0–23, e.g. 8 = 08:00)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = winEnd,
                        onValueChange = { winEnd = it.filter { c -> c.isDigit() } },
                        label = { Text("End Hour (0–23, e.g. 22 = 22:00)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Button(
                onClick = {
                    onSave(
                        ReminderPreferences.ReminderSettings(
                            enabled         = enabled,
                            intervalHours   = interval.toIntOrNull()?.coerceIn(1, 24) ?: 2,
                            windowStartHour = winStart.toIntOrNull()?.coerceIn(0, 23) ?: 8,
                            windowEndHour   = winEnd.toIntOrNull()?.coerceIn(0, 23) ?: 22
                        )
                    )
                    showSaved = true
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Reminder Settings") }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}