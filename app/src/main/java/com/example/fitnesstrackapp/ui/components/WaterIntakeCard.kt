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
fun WaterIntakeCard(
    waterIntake: Int,
    onAddGlass: () -> Unit
) {

    val progress = (waterIntake / 8f).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0F2FE)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "💧 Water Intake",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF0369A1)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$waterIntake / 8 Glasses",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF3B82F6),
                trackColor = Color.Gray.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${(progress * 100).toInt()}% Hydrated",
                color = Color(0xFF0369A1)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAddGlass,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6)
                )
            ) {
                Text("➕ Add Glass")
            }
        }
    }
}