package com.example.fitnesstrackapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnesstrackapp.Repository.FitnessRepository

class FitnessViewModelFactory(
    private val repository: FitnessRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(FitnessViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FitnessViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}