package com.example.maplecinema.domain.repository


import android.util.Log
import com.example.maplecinema.domain.model.Item
import com.example.maplecinema.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: ApiService) {
    suspend fun search(keyword: String, limit: Int = 100, page: Int = 1): List<Item> {
        return withContext(Dispatchers.IO) {
            try {
                api.search(keyword, limit, page).data.items
            } catch (e: Exception) {
                Log.e("SearchRepository", "search: $e", )
                emptyList()
            }
        }
    }


}