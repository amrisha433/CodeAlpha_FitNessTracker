package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WaterIntakeCard(
    totalMl: Int,
    goalMl: Int,
    onAddMl: (Int) -> Unit,
    onUndo: () -> Unit
) {
    val progress = if (goalMl > 0) (totalMl.toFloat() / goalMl).coerceIn(0f, 1f) else 0f
    val percentInt = (progress * 100).toInt()

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
                text = "💧 Water Intake",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${totalMl}ml / ${goalMl}ml",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF3B82F6),
                trackColor = Color.Gray.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$percentInt% Hydrated",
                color = Color(0xFF0369A1)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick-add row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(150, 250, 500).forEach { ml ->
                    FilledTonalButton(
                        onClick = { onAddMl(ml) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+${ml}ml", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            TextButton(
                onClick = onUndo,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("↩ Undo last entry")
            }
        }
    }
}