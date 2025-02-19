package com.example.maplecinema.domain.repository


import Episode
import MovieDetail
import MovieOfDetailMovie
import com.example.maplecinema.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDetailRepository @Inject constructor(private val api: ApiService) {

    suspend fun getMovieDetail(slugMovie: String): MovieDetail {

        return withContext(Dispatchers.IO) {
            try {
                api.getMovieDetail(slugMovie)

            } catch (e: Exception) {
               MovieDetail(status = false, msg = e.toString(), movie = MovieOfDetailMovie(), episodes = emptyList())
            }
        }
    }

}