package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.Data.Workout
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*

private val CATEGORIES = listOf(
    "All", "Running", "Cycling", "Walking", "Yoga", "Gym", "Swimming"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreen(viewModel: FitnessViewModel) {

    val allWorkouts by viewModel.workouts.collectAsState()

    // ── Filters ──────────────────────────────────────────────────────────────
    var selectedCategory by remember { mutableStateOf("All") }
    var showFilters      by remember { mutableStateOf(false) }

    val filtered = remember(allWorkouts, selectedCategory) {
        if (selectedCategory == "All") allWorkouts
        else allWorkouts.filter { it.category == selectedCategory }
    }

    // Group by local calendar date (newest first)
    val groupedByDate = remember(filtered) {
        filtered
            .groupBy { workout ->
                Instant.ofEpochMilli(workout.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
            .entries
            .sortedByDescending { it.key }
    }

    // ── Dialogs ──────────────────────────────────────────────────────────────
    var workoutToDelete by remember { mutableStateOf<Workout?>(null) }
    var workoutToEdit   by remember { mutableStateOf<Workout?>(null) }

    // Delete confirmation dialog
    workoutToDelete?.let { w ->
        AlertDialog(
            onDismissRequest = { workoutToDelete = null },
            title            = { Text("Delete Workout?") },
            text             = { Text("\"${w.name}\" will be permanently removed from your history.") },
            confirmButton    = {
                TextButton(onClick = {
                    viewModel.deleteWorkout(w)
                    workoutToDelete = null
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { workoutToDelete = null }) { Text("Cancel") }
            }
        )
    }

    // Edit dialog
    workoutToEdit?.let { w ->
        EditWorkoutDialog(
            workout   = w,
            onDismiss = { workoutToEdit = null },
            onSave    = { updated ->
                viewModel.updateWorkout(updated)
                workoutToEdit = null
            }
        )
    }

    // ── Main UI ──────────────────────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "🏋️ Workout History",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showFilters = !showFilters }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter")
            }
        }

        Text(
            "${allWorkouts.size} total · ${filtered.size} shown",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Filter chips
        AnimatedVisibility(visible = showFilters) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Filter by category:", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CATEGORIES) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick  = { selectedCategory = cat },
                            label    = { Text(cat) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filtered.isEmpty()) {
            EmptyHistoryState(selectedCategory)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                groupedByDate.forEach { (date, dayWorkouts) ->
                    // Date header
                    item(key = "header_$date") {
                        val sdf = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
                        val dateStr = try {
                            sdf.format(Date(dayWorkouts.first().date))
                        } catch (_: Exception) { date.toString() }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Workout cards for this day
                    items(dayWorkouts, key = { it.id }) { workout ->
                        HistoryWorkoutCard(
                            workout  = workout,
                            onEdit   = { workoutToEdit = workout },
                            onDelete = { workoutToDelete = workout }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryWorkoutCard(
    workout: Workout,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = workout.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text  = "${workout.category}  •  ⏱ ${workout.duration} min  •  🔥 ${workout.calories} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit workout",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete workout",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun EmptyHistoryState(selectedCategory: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📭", style = MaterialTheme.typography.displayMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (selectedCategory == "All")
                    "No workout history yet.\nAdd workouts from the Home screen."
                else
                    "No \"$selectedCategory\" workouts recorded yet.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

// ── Edit Dialog ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditWorkoutDialog(
    workout: Workout,
    onDismiss: () -> Unit,
    onSave: (Workout) -> Unit
) {
    var name     by remember { mutableStateOf(workout.name) }
    var duration by remember { mutableStateOf(workout.duration.toString()) }
    var calories by remember { mutableStateOf(workout.calories.toString()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(workout.category) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Running", "Cycling", "Walking", "Yoga", "Gym", "Swimming")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Workout") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Workout Name") },
                    isError = name.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = { selectedCategory = cat; expanded = false }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it.filter { c -> c.isDigit() } },
                    label = { Text("Duration (min)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = duration.toIntOrNull().let { it == null || it <= 0 },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it.filter { c -> c.isDigit() } },
                    label = { Text("Calories") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = calories.toIntOrNull().let { it == null || it < 0 },
                    modifier = Modifier.fillMaxWidth()
                )

                errorMsg?.let {
                    Text(it, color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val dur = duration.toIntOrNull()
                val cal = calories.toIntOrNull()
                when {
                    name.isBlank()           -> errorMsg = "Name cannot be empty"
                    dur == null || dur <= 0  -> errorMsg = "Duration must be a positive number"
                    cal == null || cal < 0   -> errorMsg = "Calories cannot be negative"
                    else -> onSave(
                        workout.copy(
                            name     = name.trim(),
                            category = selectedCategory,
                            duration = dur,
                            calories = cal
                        )
                    )
                }
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}