package com.example.maplecinema.feature.play_movie.components

import ServerData
import android.content.Context
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi

import com.example.maplecinema.config.video_player.InitAndroidView
import com.example.maplecinema.config.video_player.InitExoPlayer


@OptIn(UnstableApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun VideoPlayer(
    currentEpisode: ServerData?,
    nextEpisode: () -> Unit,
) {

    val videoUrl = currentEpisode?.linkM3U8 ?: ""
    val context: Context = LocalContext.current

    val isBuffering = remember { mutableStateOf(false) }
    val progress = remember { mutableFloatStateOf(0f) }
    val duration = remember { mutableLongStateOf(0L) }
    val progressTime = remember { mutableLongStateOf(0L) }
    val enableController = remember { mutableStateOf(false) }
    val onAction = remember { mutableStateOf(false) }
    val exoPlayer = InitExoPlayer(
        context,
        videoUrl,
        isBuffering,
        progress,
        duration,
        progressTime,
        enableController
    )
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        InitAndroidView(exoPlayer, enableController)
        // Custom Controller
        VideoController(
            currentEpisode,
            nextEpisode,
            onAction,
            enableController,
            progress,
            duration,
            progressTime,
            exoPlayer,
            isBuffering
        )
        //  seletionPannel(modifier = Modifier.fillMaxSize())

    }


}





