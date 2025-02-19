package com.example.maplecinema.feature.home

import Item
import MovieDetail
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.maplecinema.uitils.Constants
import com.example.maplecinema.domain.model.Movie
import com.example.maplecinema.ui.components.AppBar
import com.example.maplecinema.ui.components.ShimmerLoading
import com.example.maplecinema.ui.state.UiState
import com.valentinilk.shimmer.shimmer


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    openDetailScreen: (String) -> Unit,
    openPlayMovieBanner: (String) -> Unit,
    openSearchScreen: () -> Unit,
    page: Int
) {
    val scrollState = rememberLazyListState()
    val state by viewModel.state
    LaunchedEffect(page) {
        viewModel.fetchMovie(page)
    }
    val progress by remember {
        derivedStateOf {
            val firstVisibleItemScroll = scrollState.firstVisibleItemScrollOffset.toFloat()
            val firstVisibleIndex = scrollState.firstVisibleItemIndex.toFloat()
            val totalScroll = firstVisibleIndex * 100 + firstVisibleItemScroll
            (totalScroll / 1000).coerceIn(0f, 1f)
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue = lerp(Color(0xFF296172), Color.Black, progress),
        label = "Background Animation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(state = scrollState) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .background(Color(0xFF3A8AA2))
                    )
                    CategoryTabs()

                    when (state) {
                        is UiState.Loading -> {
                            val fakeData = HomeState()
                            HomeBody(fakeData, openDetailScreen, openPlayMovieBanner, true)
                        }

                        is UiState.Error -> ErrorMessage(message = (state as UiState.Error).message)
                        is UiState.Success -> {
                            val homeState = state as UiState.Success<HomeState>
                            val data = homeState.data
                            HomeBody(data, openDetailScreen, openPlayMovieBanner, false)
                        }
                    }
                }
            }
            AppBar(
                onIconThirdClick = openSearchScreen,
                modifier = Modifier.background(color = Color.Black.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun HomeBody(
    data: HomeState,
    openDetailScreen: (String) -> Unit,
    openPlayMovieBanner: (String) -> Unit,
    isLoading: Boolean
) {
    if (isLoading) {
        ShimmerLoading(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1 / 2f)
        )
        return
    }
    Banner(
        movieDetail = data.movieBanner,
        openPlayeMovieBanner = openPlayMovieBanner
    )
    Spacer(modifier = Modifier.height(20.dp))

    ContentSection(
        movies = data.newReleaseOnListMovie,
        sectionTitle = "Mới trên Netflix",
        openDetailScreen = openDetailScreen
    )
    Spacer(modifier = Modifier.height(50.dp))
    ContentMovies(
        movies = data.movie,
        sectionTitle = "Phim Lẻ",
        openDetailScreen = openDetailScreen
    )
    Spacer(modifier = Modifier.height(50.dp))
    ContentMovies(
        movies = data.movieSeries,
        sectionTitle = "Phim Bộ",
        openDetailScreen = openDetailScreen
    )
    Spacer(modifier = Modifier.height(50.dp))
    ContentMovies(
        movies = data.cartoons,
        sectionTitle = "Phim Hoạt Hình",
        openDetailScreen = openDetailScreen
    )
    Spacer(modifier = Modifier.height(50.dp))
    ContentMovies(
        movies = data.tvSeries,
        sectionTitle = "TV Show",
        openDetailScreen = openDetailScreen
    )

}

@Composable
fun CategoryTabs(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 15.dp,
                bottom = 30.dp,
                start = 10.dp,
                end = 10.dp
            ), horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf("Phim Lẻ", "Phim Bộ", "Hoạt Hình", "TV Shows").forEach { category ->
            Text(
                text = category,
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 1.sp,
                modifier = Modifier

                    .border(1.dp, Color.White, shape = RoundedCornerShape(15.dp))
                    .padding(horizontal = 15.dp, vertical = 8.dp)

            )


        }
    }
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    movieDetail: MovieDetail,
    openPlayeMovieBanner: (String) -> Unit
) {


    Box(
        modifier = modifier
            .fillMaxWidth()

            .padding(horizontal = 20.dp)
            .aspectRatio(0.7f)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(15.dp)
            )
            .shadow(
                elevation = (50).dp,
                shape = RoundedCornerShape(10.dp),

                spotColor = Color.Black
            )


    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color(0xFF4F6269), shape = RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp)),
        ) {
            ShimmerImage(movieDetail.movie.posterUrl)

        }
        Surface(
            color = Color.Transparent,
            shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
            modifier = Modifier
                .aspectRatio(1.2f)
                .align(alignment = Alignment.BottomCenter),
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(0.5f)),
                        )
                    )
                    .padding(horizontal = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                movieDetail.movie.category?.let {
                    Text(
                        it.joinToString(separator = " • ") { it.name },
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp, top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Button(
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.weight(1f),
                        onClick = { movieDetail.movie.slug?.let { openPlayeMovieBanner(it) } }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Phát",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                            Text("Phát", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                    Button(shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF333742).copy(alpha = 0.8f)
                        ),
                        onClick = { /*TODO*/ }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Danh sách",
                                modifier = Modifier.size(20.dp)
                            )
                            Text("Danh Sách", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun ContentSection(sectionTitle: String, movies: List<Movie>, openDetailScreen: (String) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                sectionTitle,
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = Red),
                onClick = {
                }) { Text("Xem tất cả") }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
        ) {
            items(movies.size) {
                MovieItem(movies[it], openDetailScreen)
            }
        }
    }
}

@Composable
fun ContentMovies(sectionTitle: String, movies: List<Item>, openDetailScreen: (String) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                sectionTitle,
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = Red),
                onClick = {
                }) { Text("Xem tất cả") }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
        ) {
            items(movies.size) {
                MovieItem(movies[it], openDetailScreen)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, openDetailScreen: (String) -> Unit) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .aspectRatio(2 / 3f)
            .clip(RoundedCornerShape(8.dp))
            .clickable { openDetailScreen(movie.slug) }
    ) {
        ShimmerImage(movie.poster_url)
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

@Composable
fun ErrorMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, color = Red)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* TODO: Reload */ }) {
                Text("Thử lại")
            }
        }
    }
}

@Composable
fun ShimmerImage(url: String?) {

    var isImageLoading by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .listener(object : ImageRequest.Listener {
                override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                    isImageLoading = false
                }

                override fun onError(request: ImageRequest, result: ErrorResult) {
                    isImageLoading = false
                }
            })
            .build()

        Image(
            painter = rememberAsyncImagePainter(imageRequest),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        if (isImageLoading) {
            // Hiển thị hiệu ứng shimmer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer() // Áp dụng hiệu ứng shimmer
                    .background(Color.White.copy(alpha = 0.3f))
            )
        }


    }

}
