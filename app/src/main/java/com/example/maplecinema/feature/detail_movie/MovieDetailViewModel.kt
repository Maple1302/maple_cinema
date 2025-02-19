package com.example.maplecinema.feature.detail_movie

import MovieDetail
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maplecinema.domain.repository.MovieDetailRepository
import com.example.maplecinema.ui.state.UiState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val repo: MovieDetailRepository) :
    ViewModel() {
    private val _movieDetail = mutableStateOf<UiState<MovieDetail>?>(UiState.Loading)
    val movieDetail: State<UiState<MovieDetail>?> = _movieDetail
    suspend fun fetchMovieDetail(slugMovie: String) {
        viewModelScope.launch {
            _movieDetail.value = UiState.Loading
            try {
                val movieDetail = repo.getMovieDetail(slugMovie)
                if (!movieDetail.status) {
                    _movieDetail.value = UiState.Error("Movie detail not found")
                    return@launch
                }
                _movieDetail.value = UiState.Success(movieDetail)
            } catch (e: Exception) {
                _movieDetail.value = UiState.Error(e.message.toString())
            }
        }
    }

}