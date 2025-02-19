package com.example.maplecinema

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.maplecinema.config.router.AppNavHost
import com.example.tiktokcloneapplication.ui.theme.MapleCinemaTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapleCinemaTheme {
                MainScreen()
            }
        }
    }
}

@HiltAndroidApp
class MapleCinemaApp : Application()

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    AppNavHost(navController = navController, modifier = Modifier)
}
