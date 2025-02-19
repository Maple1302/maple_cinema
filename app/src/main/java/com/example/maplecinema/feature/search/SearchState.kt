package com.example.maplecinema.feature.search

import com.example.maplecinema.domain.model.Item


data class SearchState(

    val itemsCartoon: List<Item> = emptyList(),
    val itemsSeries: List<Item> = emptyList(),
    val itemsSingle: List<Item> = emptyList(),
    val itemsTvShows: List<Item> = emptyList(),
    val isEmpty:Boolean =true,
    val endReached: Boolean = false,
    val page: Int = 0
)
