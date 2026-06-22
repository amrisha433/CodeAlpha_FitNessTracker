package com.example.fitnesstrackapp.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstrackapp.Data.FitnessDatabase
import com.example.fitnesstrackapp.Repository.FitnessRepository
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel
import com.example.fitnesstrackapp.ViewModel.FitnessViewModelFactory
import com.example.fitnesstrackapp.ui.Screen.HomeScreen
import com.example.fitnesstrackapp.ui.Screen.MainScreen

@Composable
fun FitnessTrackerApp() {

    val context = LocalContext.current

    val database = remember {
        FitnessDatabase.getDatabase(context)
    }

    val repository = remember {
        FitnessRepository(database.workoutDao())
    }

    val factory = remember {
        FitnessViewModelFactory(repository)
    }

    val viewModel: FitnessViewModel = viewModel(
        factory = factory
    )

    MainScreen(
        viewModel = viewModel
    )
}