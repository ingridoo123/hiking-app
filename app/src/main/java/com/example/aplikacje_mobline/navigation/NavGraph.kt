package com.example.aplikacje_mobline.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.aplikacje_mobline.presentation.favorite.FavoritesScreen
import com.example.aplikacje_mobline.presentation.home.HomeScreen
import com.example.aplikacje_mobline.presentation.trail.TrailDetailsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarRoutes = setOf(Screen.Home.route, Screen.Favorites.route)
    val shouldShowBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            // TODO: Replace with your custom icon path/painterResource.
                            Icon(Icons.Filled.Home, contentDescription = "Ekran glowny")
                        },
                        label = { Text("Glowna") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Favorites.route,
                        onClick = {
                            navController.navigate(Screen.Favorites.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            // TODO: Replace with your custom icon path/painterResource.
                            Icon(Icons.Filled.Favorite, contentDescription = "Ulubione trasy")
                        },
                        label = { Text("Ulubione") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(route = Screen.Favorites.route) {
                FavoritesScreen()
            }
            composable(
                route = Screen.TrailDetails.route,
                arguments = listOf(navArgument("trailId") { type = NavType.StringType })
            ) { backStackEntry ->
                val trailId = backStackEntry.arguments?.getString("trailId")
                TrailDetailsScreen(
                    trailId = trailId?.toInt() ?: 0,
                    onBackClick = { navController.navigateUp() },
                    onFavoriteClick = { /* TODO: Add save-to-favorites action. */ }
                )
            }
        }
    }
}