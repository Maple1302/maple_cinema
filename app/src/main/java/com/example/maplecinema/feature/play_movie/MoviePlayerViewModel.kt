package com.example.maplecinema.feature.play_movie

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
class MoviePlayerViewModel @Inject constructor(private val repo: MovieDetailRepository) :
    ViewModel() {
    private val _state = mutableStateOf<UiState<MoviePlayerState>>(UiState.Loading)
    val state: State<UiState<MoviePlayerState>> = _state

    fun fetchEpisode(slugMovie: String, currentEpisode: Int) {
        try {
            _state.value = UiState.Loading
            viewModelScope.launch {
                val movieDetail = repo.getMovieDetail(slugMovie)

                _state.value = UiState.Success(
                    MoviePlayerState(
                        movieDetail,
                        nextEpisode = currentEpisode < movieDetail.episodes.size - 1,
                        previousEpisode = currentEpisode >0,
                        currentEpisodeIndex = currentEpisode,
                        currentEpisode = movieDetail.episodes[0].serverData[currentEpisode]
                    )
                )
            }
        } catch (e: Exception) {
            _state.value = UiState.Error(e.message.toString())
        }

    }
}