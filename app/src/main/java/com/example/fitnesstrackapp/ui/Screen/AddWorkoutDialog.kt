package com.example.fitnesstrackapp.ui.Screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddWorkoutDialog(
    onDismiss: () -> Unit,
    onSave: (String, Int, Int) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Add Workout")
        },

        text = {

            Column {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text("Workout Name")
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = {
                        Text("Duration (min)")
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = {
                        Text("Calories")
                    }
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {
                    val durationInt = duration.toIntOrNull()
                    val caloriesInt = calories.toIntOrNull()

                    if (
                        name.isNotBlank() &&
                        duration.isNotBlank() &&
                        calories.isNotBlank()
                    ) {

                        if (
                            name.isNotBlank() &&
                            durationInt != null &&
                            caloriesInt != null
                        ) {
                            Log.d("Workout", "$name $duration $calories")
                            onSave(
                                name,
                                durationInt,
                                caloriesInt
                            )
                        }

                    }
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}