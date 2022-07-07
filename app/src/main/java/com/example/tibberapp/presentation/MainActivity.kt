package com.example.tibberapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tibberapp.presentation.powerupdetails.PowerUpDetailsScreen
import com.example.tibberapp.presentation.powerupslist.PowerUpsListScreen
import com.example.tibberapp.presentation.powerupslist.PowerUpsViewModel
import com.example.tibberapp.presentation.util.Screen
import com.example.tibberapp.ui.theme.TibberAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TibberAppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.PowerUpsListScreen.route
                    ) {
                        val viewModel: PowerUpsViewModel by viewModels()
                        composable(route = Screen.PowerUpsListScreen.route) {
                            PowerUpsListScreen(navController = navController, viewModel)
                        }
                        composable(route = Screen.PowerUpDetailsScreen.route) {
                            PowerUpDetailsScreen(
                                navController = navController,
                                viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
