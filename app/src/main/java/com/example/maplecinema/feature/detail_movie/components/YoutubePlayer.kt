package com.example.maplecinema.feature.detail_movie.components

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayer(videoId: String) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val youTubePlayerView = YouTubePlayerView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    extractYouTubeId(videoId)?.let {
                        youTubePlayer.loadVideo(
                            it,
                            0f
                        )
                        youTubePlayer.pause()


                    }
                }
            })

            youTubePlayerView
        }
    )
}

fun extractYouTubeId(url: String): String? {
    val uri = Uri.parse(url)
    return when {
        uri.host == "youtu.be" -> uri.lastPathSegment // youtu.be/nmIAlezi9mY
        uri.getQueryParameter("v") != null -> uri.getQueryParameter("v") // youtube.com/watch?v=nmIAlezi9mY
        uri.pathSegments.contains("embed") -> uri.lastPathSegment // youtube.com/embed/nmIAlezi9mY
        else -> null
    }
}
