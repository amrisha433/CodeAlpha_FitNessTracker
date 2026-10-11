package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Displays today's real sensor-derived step count.
 *
 * Shows appropriate states for:
 * - Permission not granted (ACTIVITY_RECOGNITION)
 * - Sensor not available on device
 * - Normal counting display
 *
 * Note: This card does NOT have a "Add steps" button. Steps come exclusively
 * from the hardware TYPE_STEP_COUNTER sensor. Foreground-only tracking is used
 * (steps accumulate while the app is open); this limitation is disclosed in the
 * supporting text.
 */
@Composable
fun StepTrackerCard(
    steps: Int,
    goal: Int,
    permissionGranted: Boolean,
    sensorAvailable: Boolean
) {
    val progress = if (goal > 0) (steps.toFloat() / goal).coerceIn(0f, 1f) else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "👣 Step Tracker",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                !permissionGranted -> {
                    Text(
                        "Activity recognition permission is required to count steps.\n" +
                        "Please grant the permission in Settings to enable step tracking.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                !sensorAvailable -> {
                    Text(
                        "⚠️ This device does not have a built-in step counter sensor.\n" +
                        "Step tracking is not available.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    Text(text = "$steps / $goal Steps")

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF22C55E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${(progress * 100).toInt()}% of daily goal",
                        color = Color(0xFF15803D)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Steps counted while the app is open.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}