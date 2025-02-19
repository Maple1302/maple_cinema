package com.example.maplecinema.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ListMovie(
    val status: Boolean,
    val items: List<Movie>,
    val pagination: Pagination
)

@Serializable
data class Movie(
    val modified: Modified,
    val _id: String, // Use _id or id, be consistent with your JSON
    val name: String,
    val slug: String,
    val origin_name: String, // Use snake_case to match your JSON
    val poster_url: String, // Use snake_case to match your JSON
    val thumb_url: String, // Use snake_case to match your JSON
    val year: Int
)

@Serializable
data class Modified(
    val time: String?="" // Or Instant
)

@Serializable
data class Pagination(
    val totalItems: Int=0,
    val totalItemsPerPage: Int=0,
    val currentPage: Int=0,
    val totalPages: Int=0
)

