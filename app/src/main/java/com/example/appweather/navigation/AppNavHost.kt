package com.example.appweather.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appweather.ui.screens.location.LocationScreen
import com.example.appweather.ui.screens.SettingsScreen
import com.example.appweather.ui.screens.weather.WeatherScreen

sealed class Screen(val route: String) {
    object Weather : Screen("weather")
    object Location : Screen("location")
    object Settings : Screen("settings")
    object Map : Screen("map")
}

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route,
    ) {
        composable(
            route = Screen.Weather.route,
            exitTransition = {
                if (targetState.destination.route == Screen.Location.route) {
                    slideOutHorizontally(
                        targetOffsetX = { it / 3 },
                        animationSpec = tween(durationMillis = 300)
                    )
                } else null
            },
            popEnterTransition = {
                if (initialState.destination.route == Screen.Location.route) {
                    slideInHorizontally(
                        initialOffsetX = { it / 3 },
                        animationSpec = tween(durationMillis = 300)
                    )
                } else null
            }
        ) {
            WeatherScreen(
                onOpenLocation = { navController.navigate(Screen.Location.route) },
                onOpenSettings = { navController.navigate(Screen.Settings.route) },
                onOpenMap = { navController.navigate(Screen.Map.route) },
            )
        }

        composable(
            route = Screen.Location.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = 300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(durationMillis = 300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = 300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) {
            LocationScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.Map.route) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Map screen todo")
            }
        }
    }
}
