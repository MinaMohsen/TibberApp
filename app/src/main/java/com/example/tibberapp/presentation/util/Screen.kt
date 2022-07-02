package com.example.tibberapp.presentation.util

sealed class Screen(val route: String) {
    object PowerUpsListScreen : Screen("power_ups_screen")
    object PowerUpDetailsScreen : Screen("power_up_details_screen")
}
