package com.example.aplikacje_mobline.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("home")
    data object Map: Screen("map")
    data object TrailDetails: Screen("trailDetails/{trailId}") {
        fun createRoute(trailId: Int) = "trailDetails/$trailId"
    }

}