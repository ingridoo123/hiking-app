package com.example.aplikacje_mobline.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aplikacje_mobline.presentation.home.HomeScreen
import com.example.aplikacje_mobline.presentation.trail.TrailDetailsScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Map.route) {
            //MapScreen(navController = navController)
        }
        composable(
            route = Screen.TrailDetails.route,
            arguments = listOf(navArgument("trailId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trailId = backStackEntry.arguments?.getString("trailId")
            TrailDetailsScreen(trailId?.toInt() ?: 0)
        }

    }
}