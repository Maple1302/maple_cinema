package com.example.maplecinema.feature.play_movie

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.maplecinema.feature.home.ErrorMessage
import com.example.maplecinema.feature.play_movie.components.VideoPlayer
import com.example.maplecinema.ui.state.UiState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MoviePlayerScreen(
    // currentEpisode: ServerData?,
    slug: String,
    currentIndex: Int = 0,
    onBack: () -> Unit,
    nextEpisode: () -> Unit,
    viewModel: MoviePlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val systemUiController = rememberSystemUiController()
    var isSystemUiVisible by remember { mutableStateOf(false) }
    LaunchedEffect(slug) {
        viewModel.fetchEpisode(slugMovie = slug, currentEpisode = currentIndex)
    }
    LaunchedEffect(isSystemUiVisible) {
        if (isSystemUiVisible) {
            delay(3000)
            isSystemUiVisible = false
        }
    }

    LaunchedEffect(isSystemUiVisible) {
        systemUiController.isSystemBarsVisible = isSystemUiVisible
    }

    LaunchedEffect(Unit) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    DisposableEffect(Unit) {
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            systemUiController.isSystemBarsVisible = true
        }
    }


    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center

    ) {
        val state by viewModel.state
        when (state) {
            is UiState.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )
            }

            is UiState.Error -> {
                ErrorMessage((state as UiState.Error).message)
            }
            is UiState.Success -> {
                val data = (state as UiState.Success<MoviePlayerState>).data
                data.currentEpisode?.let {
                    VideoPlayer(
                        nextEpisode = nextEpisode,
                        currentEpisode = it
                    )
                }
            }
        }


    }
}
