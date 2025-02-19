package com.example.maplecinema.feature.play_movie.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun EpisodeSelectionPanel() {

    val scrollState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(0.7f)
            .background(Color.Gray), state = scrollState
    ) {
        stickyHeader {
            Text(
                "name",
                modifier = Modifier.background(Color.Gray),
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        item {

            Text(
                "name",
                modifier = Modifier.background(Color.Gray),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")
            Text("item")

        }

    }

}

@Composable
fun seletionPannel(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(0.7f), contentAlignment = Alignment.CenterEnd
    ) {
        EpisodeSelectionPanel()
    }
}

@Preview(showBackground = true, backgroundColor = 151515)
@Composable
fun ts() {
    Box(
        modifier = Modifier.size(width = 250.dp, height = 100.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 5.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                Modifier
                    .fillMaxHeight(0.3f)
                    .fillMaxWidth()
                    .background(Color.Red)
            ) // Item 1
            Box(
                Modifier
                    .fillMaxHeight(0.25f)
                    .fillMaxWidth()
                    .background(Color.Green)
            ) // Item 2
            Box(
                Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
                    .background(Color.Blue)
            ) // Item 3
        }

        // Item 4 (nằm trên bên phải Item 3)
        Box(
            Modifier
                .size(width = (250 * 0.5).dp, height = (100 * 0.625).dp) // 125x62.5
                .align(Alignment.CenterEnd)
                .background(Color.Yellow)
        )
    }

}