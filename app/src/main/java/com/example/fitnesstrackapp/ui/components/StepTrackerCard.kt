package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StepTrackerCard(
    steps: Int,
    goal: Int = 10000,
    onAddSteps: () -> Unit
) {

    val progress =
        (steps.toFloat() / goal)
            .coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0FDF4)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )

    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "👣 Step Tracker",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF15803D)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$steps / $goal Steps"
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF22C55E)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${(progress * 100).toInt()}% Completed",
                color = Color(0xFF15803D)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAddSteps,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF22C55E)
                )
            ) {
                Text("➕ Add 500 Steps")
            }
        }
    }
}