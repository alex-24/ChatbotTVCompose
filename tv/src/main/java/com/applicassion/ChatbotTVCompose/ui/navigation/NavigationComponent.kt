package com.applicassion.ChatbotTVCompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.applicassion.ChatbotTVCompose.ui.screens.home.HomeScreen

enum class NavRoutes {
    HomeScreen,
}

@Composable
fun NavigationComponent() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HomeScreen.name
    ) {
        composable(route = "${NavRoutes.HomeScreen.name}") {
            HomeScreen()
        }
    }
}