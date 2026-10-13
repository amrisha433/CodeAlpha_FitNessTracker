package com.example.fitnesstrackapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstrackapp.Navigation.FitnessTrackerApp
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel
import com.example.fitnesstrackapp.ViewModel.FitnessViewModelFactory
import com.example.fitnesstrackapp.ui.theme.FitnessTrackAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessTrackAppTheme {
                PermissionAwareApp()
            }
        }
    }
}
/**
 * Requests runtime permissions then initialises the step counter once granted.
 * Handles permission denial gracefully – the app continues to work without step data.
 */
@Composable
private fun PermissionAwareApp() {
    val context = LocalContext.current
    val factory = remember { FitnessViewModelFactory(context.applicationContext) }
    val viewModel: FitnessViewModel = viewModel(factory = factory)

    // ── Activity Recognition permission (step counter, API 29+) ──────────────
    var activityPermGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) ==
                        PackageManager.PERMISSION_GRANTED
            } else true  // not required below Q
        )
    }

    val activityPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        activityPermGranted = granted
        viewModel.initStepCounter(context, granted)
    }

    // ── Notification permission (hydration reminders, API 33+) ───────────────
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notifPermGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        val notifPermLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { /* graceful – reminders simply won't post if denied */ }

        LaunchedEffect(Unit) {
            if (!notifPermGranted) {
                notifPermLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // ── Request activity recognition and initialise sensor ───────────────────
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !activityPermGranted) {
            activityPermLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            viewModel.initStepCounter(context, activityPermGranted)
        }
    }

    // ── Unregister sensor listener when activity is no longer visible ─────────
    DisposableEffect(Unit) {
        onDispose {
            viewModel.unregisterStepSensor()
        }
    }

    FitnessTrackerApp()
}
