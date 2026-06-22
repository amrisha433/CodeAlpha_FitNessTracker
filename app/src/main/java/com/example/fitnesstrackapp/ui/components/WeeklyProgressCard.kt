package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun WeeklyProgressCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFBEB)
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "📅 Weekly Progress",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFB45309)
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProgressItem("Mon", 0.3f)
            ProgressItem("Tue", 0.5f)
            ProgressItem("Wed", 0.2f)
            ProgressItem("Thu", 0.7f)
            ProgressItem("Fri", 0.9f)
            ProgressItem("Sat", 0.6f)
            ProgressItem("Sun", 0.8f)
        }
    }
}

@Composable
fun ProgressItem(
    day: String,
    progress: Float
) {

    Text(
        text = day,
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.height(4.dp))

    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF59E0B),
        trackColor = Color.Gray.copy(alpha = 0.2f)
    )

    Spacer(modifier = Modifier.height(10.dp))
}