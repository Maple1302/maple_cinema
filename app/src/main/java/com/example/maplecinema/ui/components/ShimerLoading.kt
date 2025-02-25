package com.example.maplecinema.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shimmer()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray.copy(alpha = 0.5f))
    )
}

