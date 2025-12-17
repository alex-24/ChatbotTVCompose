package com.applicassion.ChatbotTVCompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.applicassion.ChatbotTVCompose.ui.screens.allapps.AllAppsScreen
import com.applicassion.ChatbotTVCompose.ui.screens.home.AppsViewModel
import com.applicassion.ChatbotTVCompose.ui.screens.home.HomeScreen

enum class NavRoutes {
    HomeScreen,
    AllAppsScreen,
}

// Provide NavController to nested composables
val LocalNavController = staticCompositionLocalOf<NavController> {
    error("No NavController provided")
}

// Provide shared AppsViewModel
val LocalAppsViewModel = staticCompositionLocalOf<AppsViewModel> {
    error("No AppsViewModel provided")
}

@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    val appsViewModel: AppsViewModel = hiltViewModel()

    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalAppsViewModel provides appsViewModel
    ) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HomeScreen.name
        ) {
            composable(route = NavRoutes.HomeScreen.name) {
                HomeScreen(
                    onNavigateToAllApps = {
                        navController.navigate(NavRoutes.AllAppsScreen.name)
                    }
                )
            }
            composable(route = NavRoutes.AllAppsScreen.name) {
                AllAppsScreen(
                    onBackPress = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}