package com.example.maplecinema.feature.play_movie.components

import Episode
import ServerData
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.source.LoadEventInfo
import androidx.media3.exoplayer.source.MediaLoadData
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import isNetworkAvailable
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun VideoPlayer(
    currentEpisode: ServerData?,
    nextEpisode: () -> Unit,
) {

    val videoUrl = currentEpisode?.linkM3U8 ?: ""
    val context: Context = LocalContext.current
    val loadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(
            30_000,
            60_000,
            15_000,
            30_000
        )
        .build()
    val trackSelector = remember {
        DefaultTrackSelector(context).apply {
            setParameters(
                buildUponParameters()
                    .setMaxVideoBitrate(5000_000) // 5Mbps
                    .setForceLowestBitrate(true) // Giảm tốc độ tải khi mạng yếu
            )
        }
    }
    val cacheDir = context.cacheDir
    val availableMemory = remember {
        val memoryInfo = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(memoryInfo)
        memoryInfo.availMem
    }

    val cacheSize = if (availableMemory > 200 * 1024 * 1024) { // Nếu RAM > 200MB, dùng 100MB Cache
        100 * 1024 * 1024
    } else { // Nếu RAM thấp, giảm cache
        50 * 1024 * 1024
    }

    val databaseProvider = remember { StandaloneDatabaseProvider(context) }
    val cache = remember {
        SimpleCache(cacheDir, LeastRecentlyUsedCacheEvictor(cacheSize.toLong()), databaseProvider)
    }

    val cacheDataSourceFactory = remember {
        CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
    }
    val exoPlayer = remember {
        ExoPlayer.Builder(context).setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .build()
            .apply {
                val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(videoUrl))
                setMediaSource(mediaSource)
                setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
                prepare()
                playWhenReady = true
            }
    }
    var totalDataUsed by remember { mutableLongStateOf(0L) }
    exoPlayer.addAnalyticsListener(object : AnalyticsListener {
        override fun onLoadCompleted(
            eventTime: AnalyticsListener.EventTime,
            loadEventInfo: LoadEventInfo,
            mediaLoadData: MediaLoadData
        ) {
            val videoBytes = loadEventInfo.bytesLoaded
            totalDataUsed += videoBytes
            Log.d("NetworkUsage", "Total Uses: ${totalDataUsed / 1024 / 1024} MB")
            Log.d("NetworkUsage", "Video loaded: ${videoBytes / 1024} KB")
        }
    })


    var resizeMode by remember { mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT) }
    var scaleFactor by remember { mutableFloatStateOf(1f) }
    var onZooming by remember { mutableStateOf(false) }
    val onAction = remember { mutableStateOf(false) }
    // Kích hoạt chế độ toàn màn hình
    val progress = remember { mutableFloatStateOf(0f) }
    val duration = remember { mutableLongStateOf(0L) }
    val progressTime = remember { mutableLongStateOf(0L) }
    val isBuffering = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val activity = context as? ComponentActivity
        activity?.window?.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.systemBars()) // Ẩn status bar & navigation bar
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE // Vuốt để hiển thị lại UI
        }
    }
    LaunchedEffect(exoPlayer) {
        while (true) {
            if (exoPlayer.duration > 0) {
                progress.floatValue = exoPlayer.currentPosition.toFloat() / exoPlayer.duration
                duration.longValue = exoPlayer.duration
                progressTime.longValue = exoPlayer.currentPosition
            }
            delay(1000)
        }
    }
    HandleLifecycleEvents(exoPlayer)
    val enableController = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var hideJob by remember { mutableStateOf<Job?>(null) }

    fun changeStateControls() {
        enableController.value = !enableController.value
        hideJob?.cancel() // Hủy job cũ nếu có
        hideJob = coroutineScope.launch {
            delay(3000) // Chờ 3 giây
            enableController.value = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val playerView = PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    this.resizeMode = resizeMode
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                }
                // Bắt sự kiện zoom

                val scaleGestureDetector = ScaleGestureDetector(
                    ctx,
                    object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                        override fun onScale(detector: ScaleGestureDetector): Boolean {
                            scaleFactor = detector.scaleFactor
                            onZooming = true
                            return true
                        }
                    })

                playerView.setOnTouchListener { view, event ->
                    scaleGestureDetector.onTouchEvent(event)
                    when (event.action) {


                        MotionEvent.ACTION_UP -> {

                                if (onZooming) {
                                    onZooming = false
                                    resizeMode = if (scaleFactor > 1) {
                                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                    } else {
                                        AspectRatioFrameLayout.RESIZE_MODE_FIT
                                    }
                                    playerView.resizeMode = resizeMode

                                    view.performClick()
                                }
                                else{
                                    changeStateControls()
                                }
                        }


                    }
                    true
                }
                playerView
            },

            update = { playerView ->
                playerView.resizeMode = resizeMode
            }
        )
        // Custom Controller
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
                VideoControls(exoPlayer, isBuffering,onAction)
                Column(Modifier.fillMaxHeight(0.5f), verticalArrangement = Arrangement.Bottom) {
                    SeekBar(
                        progress = progress.floatValue,
                        onValueChange = { newValue ->
                            exoPlayer.seekTo((newValue * duration.longValue).toLong())
                            progress.floatValue = newValue
                        },
                        progressTime = progressTime.longValue,
                        enableController = enableController
                    )
                    PlaybackOptions(exoPlayer, nextEpisode = nextEpisode)
                }

            }
        }
        //  seletionPannel(modifier = Modifier.fillMaxSize())

    }
    LaunchedEffect(Unit) {
        if (enableController.value) {
            delay(3000)
            enableController.value = false
        }
    }
    LaunchedEffect(exoPlayer) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (!isNetworkAvailable(context)) {
                    Toast.makeText(context, "Mất kết nối Internet!", Toast.LENGTH_SHORT).show()
                    enableController.value = true
                    isBuffering.value = true
                } else {
                    isBuffering.value = true
                    enableController.value = true
                    exoPlayer.prepare()

                }
                when (state) {
                    Player.STATE_READY -> {
                        if (!exoPlayer.playWhenReady) {
                            isBuffering.value = true
                            enableController.value = true
                            exoPlayer.prepare()
                            exoPlayer.play()
                            exoPlayer.playWhenReady = true
                            Log.e("playervieo", "ExoPlayer đã sẵn sàng nhưng không phát")
                        }
                        isBuffering.value = false
                        changeStateControls()
                    }

                    Player.STATE_BUFFERING -> {
                        isBuffering.value = true
                        changeStateControls()
                        Log.e("playervieo", "ExoPlayer đang buffer...")
                    }

                    Player.STATE_ENDED -> {
                        changeStateControls()
                        isBuffering.value = false
                        Log.e("playervieo", "Video ended...")
                    }

                    Player.STATE_IDLE -> {
                        Log.e(
                            "playervieo",
                            "⚠️ ExoPlayer đang ở trạng thái IDLE. Chuẩn bị lại video..."
                        )
                        exoPlayer.prepare()
                        exoPlayer.playWhenReady = true
                    }
                }
            }
        })
    }
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
            cache.release()
        }
    }
}

@Composable
fun HandleLifecycleEvents(exoPlayer: ExoPlayer) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.playWhenReady = false
                }

                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.playWhenReady = true
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}



