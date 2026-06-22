package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun ProgressScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 46.dp,horizontal = 16.dp)
    ) {

        Text(
            text = "📊 Progress Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProgressCard(
            title = "Calories Burned",
            value = "500 / 2000 kcal",
            progress = 0.25f,
            Color(0xFFEF4444)
        )

        ProgressCard(
            title = "Water Intake",
            value = "4 / 8 Glasses",
            progress = 0.5f,
            Color(0xFF2563EB)
        )

        ProgressCard(
            title = "Steps",
            value = "6500 / 10000",
            progress = 0.65f,
            Color(0xFF16A34A)
        )

        ProgressCard(
            title = "Workouts",
            value = "2 / 3",
            progress = 0.67f,
            color = Color(0xFFF59E0B)
        )
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
            .padding(bottom = 12.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEEF2FF)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),

    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(value)

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = color
            )
        }
    }
}