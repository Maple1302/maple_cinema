package com.example.maplecinema.feature.play_movie.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer
import com.example.maplecinema.R
import com.example.maplecinema.helper.formatTime

@Composable
fun SeekBar(
    enableController:MutableState<Boolean>,
    progressTime: Long,
    progress: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderWidth by remember { mutableFloatStateOf(0f) }


    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(horizontal = 30.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        enableController.value = true
                        val newProgress = (offset.x / sliderWidth).coerceIn(0f, 1f)
                        onValueChange(newProgress)

                    },
                    onDrag = { change, _ ->
                        enableController.value = true
                        val newProgress = (change.position.x / sliderWidth).coerceIn(0f, 1f)
                        onValueChange(newProgress)

                    }
                )
                detectTapGestures(
                    onTap = { offset ->
                        enableController.value = true
                        val newProgress = (offset.x / sliderWidth).coerceIn(0f, 1f)
                        onValueChange(newProgress)
                    }
                )
            }
    ) {
        sliderWidth = constraints.maxWidth.toFloat() - 60
        Column {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                // Thanh trượt (track)
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)// Độ dày của thanh trượt
                ) {
                    val trackWidth = sliderWidth * progress

                    drawRoundRect(
                        color = Color.Gray,
                        size = Size(sliderWidth, size.height),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )

                    drawRoundRect(
                        color = Color.Red,
                        size = Size(trackWidth, size.height),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                }
                // Nút kéo (thumb)
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (progress * sliderWidth).toInt(), // Giữ nguyên px, không đổi sang dp
                                y = 0
                            )
                        }
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
            }
        }
        val fontSizeTime = 14.sp
        val offsetY = with(LocalDensity.current) { fontSizeTime.toPx().toInt() } / 2
        Text(
            formatTime(progressTime),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = sliderWidth.toInt() + 10,
                        y = offsetY
                    )
                },
            style = TextStyle(
                color = Color.White,
                fontSize = fontSizeTime,
                textAlign = TextAlign.End
            )
        )
    }

}

@Composable
fun VideoControls(exoPlayer: ExoPlayer, isBuffering: MutableState<Boolean>,enableController :MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f),
        horizontalArrangement = Arrangement.SpaceEvenly,

        ) {
        VideoControlItem(R.drawable.seek_back, onClick = {
            enableController.value = true
            if (!isBuffering.value) {

                exoPlayer.seekBack()
            }

        })
        if (isBuffering.value) {
            CircularProgressIndicator(color = Color.White)
        } else {
            VideoControlItem(
                if (exoPlayer.isPlaying)
                    R.drawable.pause
                else
                    R.drawable.play,
                onClick = {
                    if (exoPlayer.isPlaying){
                        enableController.value = true
                        exoPlayer.pause()
                       }

                    else{
                        enableController.value = true
                        exoPlayer.play()
                    }

                },
            )
        }

        VideoControlItem(
            icon = R.drawable.seek_forward,
            onClick = {
                exoPlayer.playWhenReady = true
                if (!isBuffering.value) {
                    exoPlayer.seekForward()
                }
            })
    }
}

@Composable
fun VideoControlItem(icon: Int, onClick: () -> Unit, isBuffering: Boolean = false) {
    IconButton(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        onClick = onClick,
    ) {
        if (isBuffering) {
            CircularProgressIndicator(color = Color.Red)
        } else {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.padding(10.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

    }
}

@Composable
fun PlaybackOptions(exoPlayer: ExoPlayer, nextEpisode: () -> Unit) {
    Row(
        Modifier
            .fillMaxHeight(0.6f)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        PlaybackItems(
            icon = R.drawable.speedometer,
            title = "Tốc độ",
            onClick = { exoPlayer.setPlaybackSpeed(1.5f) })
        PlaybackItems(icon = R.drawable.lock, title = "Khóa màn hình") {}
        PlaybackItems(icon = R.drawable.episode, title = "Các tập khác ") {}
        PlaybackItems(icon = R.drawable.subtitle, title = "Âm thanh và phụ đề") {}
        PlaybackItems(
            icon = R.drawable.next_button,
            title = "Tập tiếp",
            onClick = { nextEpisode() })
    }
}

@Composable
fun PlaybackItems(icon: Int, title: String, onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(3.5f),
        onClick = onClick,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.padding(10.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(title, style = TextStyle(color = Color.White))
        }
    }
}
