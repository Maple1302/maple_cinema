package com.example.maplecinema.domain.repository

import Item
import android.util.Log
import com.example.maplecinema.service.ApiService
import com.example.maplecinema.domain.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListMovieRepository @Inject constructor(private val apiService: ApiService) {


    suspend fun getNewReleaseOnMovies(page: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getNewReleaseOnMovies(page).items
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun getMovies(): List<Item> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getMovie(1).data.items
            } catch (e: Exception) {
                Log.e("getMovies", "getMovies: $e")
                emptyList()
            }
        }
    }

    suspend fun getMovieSeries(page: Int): List<Item> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getMovieSeries(page).data.items
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun getTVSeries(page: Int): List<Item> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getTVSeries(page).data.items
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun getCartoons(page: Int): List<Item> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getCartoons(page).data.items
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}