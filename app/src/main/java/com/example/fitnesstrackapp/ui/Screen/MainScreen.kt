package com.example.fitnesstrackapp.ui.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.fitnesstrackapp.ViewModel.FitnessViewModel

data class NavItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun MainScreen(
    viewModel: FitnessViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val items = listOf(
        NavItem("Home",     Icons.Default.Home),
        NavItem("Progress", Icons.Default.ShowChart),
        NavItem("History",  Icons.Default.DateRange),
        NavItem("Trophies", Icons.Default.EmojiEvents),
        NavItem("Profile",  Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected    = selectedTab == index,
                        onClick     = { selectedTab = index },
                        icon        = {
                            Icon(
                                imageVector        = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Pass innerPadding to every screen so content is never hidden behind
        // the bottom navigation bar.
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> HomeScreen(viewModel = viewModel)
                1 -> ProgressScreen(viewModel = viewModel)
                2 -> WorkoutHistoryScreen(viewModel = viewModel)
                3 -> AchievementsScreen(viewModel = viewModel)
                4 -> ProfileScreen(viewModel = viewModel)
            }
        }
    }
}