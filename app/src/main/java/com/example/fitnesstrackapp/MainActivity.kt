package com.example.fitnesstrackapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fitnesstrackapp.Navigation.FitnessTrackerApp
import com.example.fitnesstrackapp.ui.Screen.HomeScreen
import com.example.fitnesstrackapp.ui.theme.FitnessTrackAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessTrackAppTheme {
                FitnessTrackerApp()
            }
        }
    }
}
