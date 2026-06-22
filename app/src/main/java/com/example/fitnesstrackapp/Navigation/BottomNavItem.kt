package com.example.fitnesstrackapp.Navigation

sealed class BottomNavItem(
    val route: String,
    val title: String
) {

    object Home : BottomNavItem(
        route = "home",
        title = "Home"
    )

    object Progress : BottomNavItem(
        route = "progress",
        title = "Progress"
    )

    object History : BottomNavItem(
        route = "history",
        title = "History"
    )


    object Profile : BottomNavItem(
        route = "profile",
        title = "Profile"
    )
}