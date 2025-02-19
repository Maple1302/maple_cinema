package com.example.maplecinema.feature.play_movie

import Episode
import MovieDetail
import ServerData

data class MoviePlayerState (
    val movieDetail: MovieDetail? = null,
    val currentEpisodeIndex: Int = 0,
    val currentEpisode: ServerData? = null,
    val nextEpisode:  Boolean? = false,
    val previousEpisode: Boolean? = false,
)