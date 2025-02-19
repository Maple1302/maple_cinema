package com.example.maplecinema.feature.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maplecinema.domain.model.Item
import com.example.maplecinema.domain.model.Type
import com.example.maplecinema.domain.repository.SearchRepository
import com.example.maplecinema.feature.home.HomeState
import com.example.maplecinema.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: SearchRepository) : ViewModel() {
    private val _state = mutableStateOf<UiState<SearchState>?>(UiState.Loading)
    val state: State<UiState<SearchState>?> = _state

    suspend fun search(keyword: String, limit: Int = 100, page: Int = 1) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val items = repo.search(keyword, limit, page)
                if (items.isEmpty()) {
                    _state.value = UiState.Success(SearchState(isEmpty = true))
                } else {
                    _state.value = UiState.Success(
                        SearchState(
                            itemsSingle = items.filter { it.type == Type.Single },
                            itemsCartoon = items.filter { it.type == Type.Cartoon },
                            itemsSeries = items.filter { it.type == Type.Series },
                            itemsTvShows = items.filter { it.type == Type.tvShows },
                            page = page,
                            endReached = items.size < limit,
                            isEmpty = false
                        )
                    )
                }
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message.toString())
            }
        }
    }
}
