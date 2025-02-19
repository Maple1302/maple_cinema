package com.example.maplecinema.config.router

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object MovieDetail : Screen("movieDetail/{slug}") {
        fun createRoute(slug: String):String = "movieDetail/$slug"
    }
    data object MoviePlayer : Screen("moviePlayer/{slug}/{index}") {
        fun createRoute(slug: String, index: Int?):String = "moviePlayer/$slug/$index"
    }
    data object Search : Screen("search")
}