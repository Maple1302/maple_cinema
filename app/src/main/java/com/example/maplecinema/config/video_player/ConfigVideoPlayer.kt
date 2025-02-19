package com.example.maplecinema.config.video_player

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.R)
@OptIn(UnstableApi::class)
@Composable
fun InitExoPlayer(context: Context, videoUrl: String,isBuffering: MutableState<Boolean>,progress: MutableState<Float>,duration: MutableState<Long>,progressTime: MutableState<Long>,enableController: MutableState<Boolean>):ExoPlayer {
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
 val exoPlayer = remember {    ExoPlayer.Builder(context).setTrackSelector(trackSelector)
        .setLoadControl(loadControl)
        .build()
        .apply {
            val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(videoUrl))
            setMediaSource(mediaSource)
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
            prepare()
            playWhenReady = true


 }}
    SetUpSystemUi(context)
    ConfigVideo(videoUrl,context,exoPlayer)
    HandelProgressVideo(exoPlayer, progress, duration, progressTime)
    HandleStatePlayVideo(exoPlayer,context,enableController,isBuffering)
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
            cache.release()
        }
    }
    HandleLifecycleEvents(exoPlayer)
    return exoPlayer
}
@Composable
@OptIn(UnstableApi::class)
fun ConfigVideo( videoUrl:String, context: Context, initPlayer: ExoPlayer){

    var totalDataUsed by remember { mutableLongStateOf(0L) }
    initPlayer.addAnalyticsListener(object : AnalyticsListener {
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

}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SetUpSystemUi(context: Context){
    LaunchedEffect(Unit) {
        val activity = context as? ComponentActivity
        activity?.window?.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.systemBars()) // Ẩn status bar & navigation bar
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE // Vuốt để hiển thị lại UI
        }
    }
}
@Composable
fun HandelProgressVideo(exoPlayer: ExoPlayer, progress: MutableState<Float>, duration: MutableState<Long>, progressTime: MutableState<Long>){
    LaunchedEffect(exoPlayer) {
        while (true) {
            if (exoPlayer.duration > 0) {
                progress.value = exoPlayer.currentPosition.toFloat() / exoPlayer.duration
                duration.value = exoPlayer.duration
                progressTime.value = exoPlayer.currentPosition
            }
            delay(1000)
        }

    }
}

@OptIn(UnstableApi::class)
@Composable
fun InitAndroidView(exoPlayer: ExoPlayer,enableController: MutableState<Boolean>){
    var resizeMode by remember { mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT) }
    var scaleFactor by remember { mutableFloatStateOf(1f) }
    var onZooming by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var hideJob by remember { mutableStateOf<Job?>(null) }

    fun changeStateControls(enableController: MutableState<Boolean> ) {
        enableController.value = !enableController.value
        hideJob?.cancel() // Hủy job cũ nếu có
        hideJob = coroutineScope.launch {
            delay(3000) // Chờ 3 giây
            enableController.value = false
        }
    }
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
                            changeStateControls(enableController)
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
}

@Composable
fun HandleStatePlayVideo(exoPlayer: ExoPlayer, context: Context, enableController: MutableState<Boolean>, isBuffering: MutableState<Boolean>){
    LaunchedEffect(exoPlayer) {
        exoPlayer.addListener(object : Player.Listener {
            @OptIn(UnstableApi::class)
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
                        enableController.value = false
                    }

                    Player.STATE_BUFFERING -> {
                        isBuffering.value = true
                        enableController.value= true
                        Log.e("playervieo", "ExoPlayer đang buffer...")
                    }

                    Player.STATE_ENDED -> {
                        enableController.value = true
                        isBuffering.value = false
                        Log.e("playervieo", "Video ended...")
                    }

                    Player.STATE_IDLE -> {
                        Log.e(
                            "playervieo",
                            "⚠️ ExoPlayer đang ở trạng thái IDLE. Chuẩn bị lại video..."
                        )
                        enableController.value= true
                        exoPlayer.prepare()
                        exoPlayer.playWhenReady = true
                    }
                }
            }
        })
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