package com.example.aplikacje_mobline.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("home")
    data object Favorites: Screen("favorites")
    data object AppInfo: Screen("appInfo")
    data object TrailDetails: Screen("trailDetails/{trailId}") {
        fun createRoute(trailId: Int) = "trailDetails/$trailId"
    }

}