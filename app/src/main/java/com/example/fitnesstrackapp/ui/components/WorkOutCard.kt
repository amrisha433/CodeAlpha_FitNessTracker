package com.example.fitnesstrackapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackapp.Data.Workout
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import com.example.fitnesstrackapp.ui.theme.CardBackground
import com.example.fitnesstrackapp.ui.theme.Green
import com.example.fitnesstrackapp.ui.theme.Navy
import com.example.fitnesstrackapp.ui.theme.Red

@Composable
fun WorkoutCard(
    workout: Workout,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
            colors = CardDefaults.cardColors(
                containerColor = CardBackground
            )

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = workout.name,
                    color = Navy,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "⏱ ${workout.duration} min",
                    color = Color.DarkGray
                )

                Text(
                    text = "🔥 ${workout.calories} kcal",
                    color = Green
                )
            }
            Spacer(modifier = Modifier.width(130.dp))

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red
                )
            ) {
                Text("Delete")
            }
        }
    }
}