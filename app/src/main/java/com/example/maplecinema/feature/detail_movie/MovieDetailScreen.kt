package com.example.maplecinema.feature.detail_movie

import Episode
import MovieDetail
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.maplecinema.R
import com.example.maplecinema.feature.detail_movie.components.YouTubePlayer
import com.example.maplecinema.feature.home.ErrorMessage
import com.example.maplecinema.feature.home.ShimmerImage
import com.example.maplecinema.feature.movie_detail.components.Button.ActionButton
import com.example.maplecinema.feature.movie_detail.components.Button.DetailButton
import com.example.maplecinema.ui.components.AppBar
import com.example.maplecinema.ui.state.UiState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    slugMovie: String,
    onPlayClick: (String, Int?) -> Unit,
    onPopBackStack: () -> Unit,
    onIconSearchClick: () -> Unit,
) {


    LaunchedEffect(slugMovie) {
        viewModel.fetchMovieDetail(slugMovie)
    }
    val movieDetail by viewModel.movieDetail
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = Color.Black)
    ) {
        stickyHeader {
            AppBar(
                iconHead = R.drawable.back_arrow,
                iconSizeHead = 30.dp,
                onIconHeadClick = onPopBackStack,
                onIconThirdClick = onIconSearchClick,
                modifier = Modifier.background(color = Color.Black.copy(alpha = 0.3f))

            )
        }
        item {
            when (movieDetail) {
                is UiState.Loading -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {

                    }
                }

                is UiState.Error -> {
                    ErrorMessage(message = (movieDetail as UiState.Error).message)
                }

                is UiState.Success -> {
                    val movieDetailData = (movieDetail as UiState.Success<MovieDetail>).data
                    Box(modifier = Modifier.fillMaxSize()) {

                        movieDetailData.let {

                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(3 / 2f)
                                ) {
                                    it.movie.trailerURL?.let { it1 ->
                                        YouTubePlayer(
                                            videoId = it1
                                        )
                                    }

                                }
                                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                                    Row(
                                        modifier = Modifier
                                            .padding(vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Absolute.Left
                                    ) {
                                        Image(

                                            painter = painterResource(R.drawable.logo),
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .width(10.dp)
                                                .aspectRatio(1 / 2f)

                                        )

                                        Text(
                                            if (it.movie.episodeTotal != "1") "Loạt phim".uppercase() else "PHIM",
                                            modifier = Modifier.padding(horizontal = 5.dp),
                                            style = TextStyle(

                                                color = Color(0xFF808080),
                                                letterSpacing = 3.sp,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }


                                    it.movie.name?.let { it1 ->
                                        Text(
                                            text = it1,
                                            style = TextStyle(
                                                color = Color.White,
                                                fontSize = 25.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        it.movie.year?.let { it1 ->
                                            Text(
                                                text = it1.toString(), style = TextStyle(
                                                    color = Color(0xFF5d5d5d),
                                                    fontSize = 15.sp,
                                                )
                                            )
                                        }
                                        it.movie.country?.get(0)?.name?.let { it1 ->
                                            Text(
                                                it1, style = TextStyle(
                                                    color = Color(0xFF5d5d5d),
                                                    fontSize = 15.sp
                                                )
                                            )
                                        }

                                        it.movie.time?.let { it1 ->
                                            Text(
                                                if (it.movie.episodeTotal == "1") convertMinutesToHours(
                                                    it1
                                                ) else it.movie.episodeCurrent + "",
                                                style = TextStyle(
                                                    color = Color(0xFF5d5d5d),
                                                    fontSize = 15.sp
                                                )
                                            )
                                        }


                                        Image(
                                            painter = painterResource(if (it.movie.quality == "FHD") R.drawable.high_definition else R.drawable.hd),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .height(15.dp)
                                                .aspectRatio(1f),
                                            contentDescription = "",
                                            colorFilter = ColorFilter.tint(color = Color.White)
                                        )


                                    }
                                    it.movie.slug?.let { it1 ->
                                        DetailButton(
                                            text = "Phát",
                                            textColor = Color.Black,
                                            icon = R.drawable.play,
                                            backgroundColor = Color.White,
                                            clickArgument = it1,
                                            onClick = { onPlayClick(it, 0) },


                                            )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    DetailButton(
                                        text = "Tải xuống",
                                        textColor = Color.White,
                                        icon = R.drawable.icon_dowload,
                                        backgroundColor = Color(0xFF262626),
                                        clickArgument = "",
                                        onClick = {}

                                    )
                                    Spacer(Modifier.height(10.dp))
                                    it.movie.content?.let {
                                        Text(it, style = TextStyle(color = Color.White))
                                    }
                                    Spacer(Modifier.height(10.dp))
                                    it.movie.actor?.let {
                                        Text(
                                            "Diễn viên: ${it.joinToString(", ")}",
                                            style = TextStyle(color = Color.White),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1
                                        )
                                    }
                                    Spacer(Modifier.height(10.dp))
                                    it.movie.director?.let {
                                        Text(
                                            "Đạo diễn: ${it.joinToString(", ")}",
                                            style = TextStyle(color = Color.White)
                                        )
                                    }
                                }
                                var selectedTab by remember { mutableStateOf(0) }
                                val isSeries = it.movie.episodeTotal != "1"
                                ActionButtonsSection()
                                MovieDetailTabs(
                                    isSeries = isSeries,
                                    selectedTabIndex = selectedTab,
                                    onTabSelected = { selectedTab = it })
                                when (selectedTab) {

                                    0 -> if (isSeries) EpisodeContent(
                                        it,
                                        onPlayClick = onPlayClick
                                    ) else CollectionContent()

                                    1 -> if (isSeries) TrailerAndMoreContent() else MoreLikeThisContent()
                                    2 -> if (!isSeries) MoreLikeThisContent()
                                }
                            }
                        }
                    }

                }

                null -> TODO()
            }
        }


    }

}

@Preview(showBackground = true, backgroundColor = 151515)
@Composable
fun EpisodesHeader(title: String? = "Tập phim") {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (title != null) {
            Text(
                title,
                modifier = Modifier.padding(vertical = 10.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            Modifier.size(18.dp)
        )
    }

}

@Composable
fun EpisodeContent(movieDetail: MovieDetail, onPlayClick: (String, Int?) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {


        EpisodesHeader(movieDetail.movie.name)
        movieDetail.episodes[0].serverData.forEachIndexed { index, it ->
            movieDetail.movie.thumbUrl?.let { it1 ->
                movieDetail.movie.time?.let { it2 ->
                    EpisodeItem(
                        it1, it.name,
                        it2, modifier = Modifier.clickable { onPlayClick(it.slug, index) }
                    )
                }
            }
        }

    }
}

@Composable
fun EpisodeItem(thumUrl: String, name: String, time: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.3f)
                            .aspectRatio(1.5f)
                            .clip(RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        ShimmerImage(thumUrl)
                        Image(
                            painter = painterResource(R.drawable.play_video_icon),
                            contentDescription = null,
                            Modifier
                                .fillMaxWidth(fraction = 0.3f)
                                .aspectRatio(1f)
                                .clip(shape = CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f)),
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.fillMaxWidth(fraction = 0.5f)) {

                        Text(
                            name,
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                        Spacer(Modifier.height(10.dp))

                        Text(
                            time.split("/")[0],
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )

                    }
                }
                Image(
                    painter = painterResource(R.drawable.icon_dowload),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 10.dp),
                    colorFilter = ColorFilter.tint(color = Color.White)
                )


            }
        }
    }
}

@Composable
fun TrailerAndMoreContent() {
    Text("Trailer & Nội dung thêm", color = Color.White)
}

@Composable
fun CollectionContent() {
    Text("Các phần khác", color = Color.White)
}

@Composable
fun MoreLikeThisContent() {
    Text("Phim tương tự", color = Color.White)
}

@Composable
fun ActionButtonsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButton(title = "Danh Sách", iconVector = Icons.Default.Add, onClick = {})
        ActionButton(title = "Đánh Giá", icon = R.drawable.like, onClick = {})
        ActionButton(title = "Chia Sẻ", icon = R.drawable.share, onClick = {})
        ActionButton(title = "Tải Xuống", icon = R.drawable.download_detail_icon, onClick = {})
    }
}


@Composable
fun MovieDetailTabs(
    isSeries: Boolean = false,
    selectedTabIndex: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {

    val tabs = if (isSeries) listOf("Tập phim", "Trailer & Thêm", "Tương tự")
    else listOf("Các Phần Khác ", "Trailer & Thêm")
    HorizontalDivider(color = Color.Gray, thickness = 2.dp)
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        contentColor = Color.White,
        divider = { HorizontalDivider(color = Color.Transparent) },
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(0.dp) // Độ dày của indicator
                    .clip(RoundedCornerShape(2.dp)), // Bo góc indicator
                color = Color.Red // Màu của indicator
            )
        },

        ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .drawBehind {
                        val strokeWidth = 4.dp.toPx()
                        val radius = 10.dp.toPx()
                        drawRoundRect(
                            color = if (selectedTabIndex == index) Color.Red else Color.Transparent,
                            topLeft = Offset(size.width * 0.05f, 0f),
                            size = Size(size.width * 0.9f, strokeWidth),
                            cornerRadius = CornerRadius(radius, radius)
                        )
                    },
                text = { Text(title, fontWeight = FontWeight.Bold) })
        }
    }
}

fun convertMinutesToHours(timeString: String): String {
    // Lấy số từ chuỗi (loại bỏ chữ "phút")
    val minutes = timeString.filter { it.isDigit() }.toIntOrNull() ?: return "Dữ liệu không hợp lệ"

    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return "$hours giờ $remainingMinutes phút"
}
