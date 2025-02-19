package com.example.maplecinema.service

import MovieDetail
import Movies
import com.example.maplecinema.domain.model.ListMovie
import com.example.maplecinema.domain.model.SearchModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("danh-sach/phim-moi-cap-nhat")
    suspend fun getNewReleaseOnMovies(@Query("page") page: Int): ListMovie

    @GET("v1/api/danh-sach/{category}")
    suspend fun getMoviesByCategory(@Path("category") category: String, @Query("page") page: Int): ListMovie

    @GET("phim/{slug_movie}")
    suspend fun getMovieDetail(@Path("slug_movie") slugMovie: String): MovieDetail

    @GET("v1/api/danh-sach/phim-le")
    suspend fun getMovie(@Query("page") page: Int): Movies

    @GET("v1/api/danh-sach/phim-bo")
    suspend fun getMovieSeries(@Query("page") page: Int): Movies

    @GET("v1/api/danh-sach/phim-bo")
    suspend fun getTVSeries(@Query("page") page: Int): Movies

    @GET("v1/api/danh-sach/hoat-hinh")
    suspend fun getCartoons(@Query("page") page: Int): Movies

    @GET("v1/api/tim-kiem")
    suspend fun search(@Query("keyword") keyword: String, @Query("limit") limit: Int,@Query("page") page: Int): SearchModel
}