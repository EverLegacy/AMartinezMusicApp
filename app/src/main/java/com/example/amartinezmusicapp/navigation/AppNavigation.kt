package com.example.amartinezmusicapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.amartinezmusicapp.ui.screens.HomeScreen
import com.example.amartinezmusicapp.ui.screens.DetailScreen

// Define app routes
sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Detail : Routes("detail/{albumId}") {
        fun createRoute(albumId: String) = "detail/$albumId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Home.route) {

        // Home screen
        composable(Routes.Home.route) {
            HomeScreen(
                onAlbumClick = { id ->
                    navController.navigate(Routes.Detail.createRoute(id))
                }
            )
        }

        // Detail screen
        composable(
            route = Routes.Detail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""
            DetailScreen(
                albumId = albumId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
