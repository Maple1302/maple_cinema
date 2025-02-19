package com.example.maplecinema.feature.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maplecinema.domain.model.Item
import com.example.maplecinema.feature.home.ShimmerImage
import com.example.maplecinema.uitils.Constants

@Composable
fun MovieItem(movie: Item, openDetailScreen: (String) -> Unit) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .aspectRatio(2 / 3f)
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color(0xFF181818))
            .clickable { openDetailScreen(movie.slug) }
    ) {
        ShimmerImage("${Constants.IMAGE_URL}${movie.posterURL}")

        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(0.5f)),
                    )

                )
                .aspectRatio(2f)
                .padding(horizontal = 5.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(

                    movie.name,
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}