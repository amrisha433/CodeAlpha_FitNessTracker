package com.example.fitnesstrackapp.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Int, Int) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    val categories = listOf(
        "Running",
        "Cycling",
        "Walking",
        "Yoga",
        "Gym",
        "Swimming"
    )

    var selectedCategory by remember {
        mutableStateOf(categories[0])
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Add Workout")
        },

        text = {

            Column {

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier.Companion
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        categories.forEach { category ->

                            DropdownMenuItem(
                                text = {
                                    Text(category)
                                },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.Companion.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text("Workout Name")
                    }
                )

                Spacer(modifier = Modifier.Companion.height(8.dp))

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = {
                        Text("Duration (min)")
                    }
                )

                Spacer(modifier = Modifier.Companion.height(8.dp))

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
                        durationInt != null &&
                        caloriesInt != null
                    ) {

                        Log.d(
                            "Workout",
                            "$selectedCategory $name"
                        )

                        onSave(
                            name,
                            selectedCategory,
                            durationInt,
                            caloriesInt
                        )
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