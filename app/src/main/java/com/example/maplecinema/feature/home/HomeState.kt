package com.example.maplecinema.feature.home

import Item
import MovieDetail
import MovieOfDetailMovie
import com.example.maplecinema.domain.model.Movie


data class HomeState(

    val isLoading: Boolean = false,
    val movie: List<Item> = emptyList(),
    val newReleaseOnListMovie: List<Movie> = emptyList(),
    val movieBanner: MovieDetail = MovieDetail(
        status = false, msg = "", movie = MovieOfDetailMovie(),
        emptyList()
    ),
    val tvSeries: List<Item> = emptyList(),
    val cartoons: List<Item> = emptyList(),
    val movieSeries: List<Item> = emptyList(),
    val errorMessages: String = ""
)