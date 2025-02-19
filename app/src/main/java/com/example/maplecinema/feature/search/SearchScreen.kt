package com.example.maplecinema.feature.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.maplecinema.R
import com.example.maplecinema.domain.model.Item
import com.example.maplecinema.feature.search.components.MovieItem
import com.example.maplecinema.ui.components.AppBar
import com.example.maplecinema.ui.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    openDetailScreen: (String) -> Unit,
    popBackStack: (String) -> Unit
) {

    val state by viewModel.state

    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LazyColumn {
            stickyHeader {
                AppBar(
                    modifier = Modifier.background(color = Color.Black),
                    iconHead = R.drawable.back_arrow,
                    onIconHeadClick = { },
                    iconThird = null,
                    iconFirst = null
                )
                SearchBar(onSearch = { query ->
                    coroutineScope.launch {
                        viewModel.search(query) // Gọi suspend function từ coroutine
                    }
                })
            }
            item {
                when (state) {
                    is UiState.Error -> {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        ) {
                            Text(text = (state as UiState.Error).message)
                        }
                    }

                    UiState.Loading -> {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        )
                    }

                    is UiState.Success -> {
                        val data = (state as UiState.Success<SearchState>).data
                        if (data.isEmpty == true) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.Black)
                            )
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                ItemSearch(
                                    data.itemsSingle,
                                    title = "Phim Lẻ",
                                    openDetailScreen = openDetailScreen
                                )
                                ItemSearch(
                                    data.itemsSeries,
                                    title = "Phim Bộ",
                                    openDetailScreen = openDetailScreen
                                )
                                ItemSearch(
                                    data.itemsCartoon,
                                    title = "Hoạt Hình",
                                    openDetailScreen = openDetailScreen
                                )
                                ItemSearch(
                                    data.itemsTvShows,
                                    title = "TV Show",
                                    openDetailScreen = openDetailScreen
                                )
                            }
                        }

                    }

                    null -> {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemSearch(
    item: List<Item>,
    title: String,
    openDetailScreen: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        )

        LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(item.size) {
                MovieItem(item[it], openDetailScreen = openDetailScreen)
            }

        }
    }
}

@Composable
fun SearchBar(onSearch: (String) -> Unit = {}) {
    var text by remember { mutableStateOf("") }
    val icon = if (text.isEmpty()) painterResource(R.drawable.voice_icon) else null
    val vectorIcon = if (text.isNotEmpty()) Icons.Default.Close else null
    LaunchedEffect(text) {
        if (text.isNotEmpty()) {
            delay(500)
            onSearch(text)
        }

    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFF272727))
    ) {


        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                text = it
            },
            textStyle = TextStyle(color = Color.White),
            maxLines = 1,
            singleLine = true,
            placeholder = {
                Text(
                    " Tìm kiếm trò chơi, tác phẩm... ",
                    style = TextStyle(color = Color.White)
                )
            },
            prefix = {
                Image(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            },
            suffix = {
                if (icon != null || vectorIcon != null) {
                    Image(
                        painter = icon
                            ?: rememberVectorPainter(vectorIcon!!),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { text = "" },
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            },
            colors = TextFieldDefaults.colors().copy(
                cursorColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }

}
