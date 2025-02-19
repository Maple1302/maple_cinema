package com.example.maplecinema.config.router

import NetworkUsageScreen
import com.example.maplecinema.feature.play_movie.MoviePlayerScreen
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.maplecinema.feature.detail_movie.MovieDetailScreen
import com.example.maplecinema.feature.home.HomeScreen
import com.example.maplecinema.feature.search.SearchScreen


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Screen.Home.route) {
            HomeScreen(openDetailScreen = { slug ->
                navController.navigate(Screen.MovieDetail.createRoute(slug))
            }, openPlayMovieBanner = { slug ->
                navController.navigate(Screen.MoviePlayer.createRoute(slug, 0))
            }, openSearchScreen = {
                navController.navigate(Screen.Search.route)
            },
                page = 1
            )

        }
        composable(Screen.MovieDetail.route) { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug") ?: "te-tuong-luu-gu"
            MovieDetailScreen(
                onPlayClick = { _, index ->
                    navController.navigate(Screen.MoviePlayer.createRoute(slug, index))
                },
                onPopBackStack = { navController.popBackStack() },
                onIconSearchClick = { navController.navigate(Screen.Search.route) },
                slugMovie = slug
            )
        }
        composable(Screen.MoviePlayer.route, arguments = listOf(
            navArgument("slug") { type = NavType.StringType },
            navArgument("index") { type = NavType.IntType }
        )) { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug")
                ?: "te-tuong-luu-gu"
            val index = backStackEntry.arguments?.getInt("index")
                ?: 0
            MoviePlayerScreen(
                slug = slug,
                currentIndex = index,
                onBack = { navController.popBackStack() },
                nextEpisode = {
                    navController.navigate(
                        Screen.MoviePlayer.createRoute(
                            slug,
                            index + 1
                        )
                    )
                })
        }
        composable(Screen.Search.route) {
            SearchScreen(openDetailScreen = { slug ->
                navController.navigate(
                    Screen.MovieDetail.createRoute(
                        slug
                    )
                )
            }, popBackStack = { navController.popBackStack() })
        }


    }
}
