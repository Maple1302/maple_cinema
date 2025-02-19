package com.example.maplecinema.feature.play_movie.components

import ServerData
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun VideoController(
    currentEpisode: ServerData?,
    nextEpisode: () -> Unit,
    onAction: MutableState<Boolean>,
    enableController: MutableState<Boolean>,
    progress: MutableState<Float>,
    duration: MutableState<Long>,
    progressTime: MutableState<Long>,
    exoPlayer: ExoPlayer,
    isBuffering: MutableState<Boolean>,
) {
    AnimatedVisibility(
        visible = enableController.value,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(vertical = 15.dp)

        ) {
            if (currentEpisode != null) {
                Text(
                    currentEpisode.name,
                    Modifier.fillMaxHeight(0.3f),
                    style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                )
            }
            VideoControls(exoPlayer, isBuffering, onAction)
            Column(Modifier.fillMaxHeight(0.5f), verticalArrangement = Arrangement.Bottom) {
                SeekBar(
                    progress = progress.value,
                    onValueChange = { newValue ->
                        exoPlayer.seekTo((newValue * duration.value).toLong())
                        progress.value = newValue
                    },
                    progressTime = progressTime.value,
                    enableController = enableController
                )
                PlaybackOptions(exoPlayer, nextEpisode = nextEpisode)
            }

        }
    }
}