package com.example.maplecinema.feature.home

import MovieDetail
import MovieOfDetailMovie
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maplecinema.domain.repository.ListMovieRepository
import com.example.maplecinema.domain.repository.MovieDetailRepository
import com.example.maplecinema.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ListMovieRepository,
    private val repoDetail: MovieDetailRepository
) : ViewModel() {
    private val _state = mutableStateOf<UiState<HomeState>>(UiState.Loading)
    val state: State<UiState<HomeState>> = _state

    fun fetchMovie(id: Int = 1) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {

                val newReleasesDeferred = async { repo.getNewReleaseOnMovies(id) }
                val moviesDeferred = async { repo.getMovies() }
                val movieSeriesDeferred = async { repo.getMovieSeries(id) }
                val tvSeriesDeferred = async { repo.getTVSeries(id) }
                val cartoonsDeferred = async { repo.getCartoons(id) }

                val newReleases = newReleasesDeferred.await()
                val movies = moviesDeferred.await()
                val movieSeries = movieSeriesDeferred.await()
                val tvSeries = tvSeriesDeferred.await()
                val cartoons = cartoonsDeferred.await()
               // val firstCartoon = cartoons.firstOrNull()
                var movieBanner= MovieDetail(episodes = emptyList(), status = false, movie = MovieOfDetailMovie(), msg = "")
                if (newReleases.firstOrNull() != null) {
                    val movieBannerDeferred = async { repoDetail.getMovieDetail(newReleases.firstOrNull()!!.slug) }
                     movieBanner = movieBannerDeferred.await()
                } else {
                    Log.e("MovieBanner", "Không có dữ liệu cartoon!")
                }
                _state.value = UiState.Success(
                    HomeState(
                        newReleaseOnListMovie = newReleases,
                        movie = movies,
                        movieSeries = movieSeries,
                        tvSeries = tvSeries,
                        cartoons = cartoons,
                        movieBanner = movieBanner
                    )
                )

            } catch (e: Exception) {
                _state.value = UiState.Error(e.message.toString())
            }
        }
    }
}