package com.example.maplecinema.di

import NetworkUsageInterceptor
import com.example.maplecinema.domain.repository.ListMovieRepository
import com.example.maplecinema.domain.repository.MovieDetailRepository
import com.example.maplecinema.domain.repository.SearchRepository
import com.example.maplecinema.service.ApiService
import com.example.maplecinema.uitils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(NetworkUsageInterceptor)
            .build()
        @OptIn(ExperimentalSerializationApi::class)
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType)).client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideListMovieRepository(apiService: ApiService): ListMovieRepository {
        return ListMovieRepository(apiService)

    }

    @Provides
    @Singleton
    fun provideMovieDetailRepository(apiService: ApiService): MovieDetailRepository {
        return MovieDetailRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideSearchMovieRepository(apiService: ApiService): SearchRepository {
        return SearchRepository(apiService)
    }
}


//@Composable
//fun NetworkUsageScreen() {
//    var receivedData by remember { mutableStateOf(0L) }
//    var transmittedData by remember { mutableStateOf(0L) }
//    val coroutineScope = rememberCoroutineScope()
//    val received = remember { NetworkUsageInterceptor.receivedBytes.get() / 1024 }
//    val sent = remember { NetworkUsageInterceptor.sentBytes.get() / 1024 }
//
//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            while (true) {
//                receivedData = TrafficStats.getUidRxBytes(Process.myUid()) // Dữ liệu nhận
//                transmittedData = TrafficStats.getUidTxBytes(Process.myUid()) // Dữ liệu gửi
//                delay(1000) // Cập nhật mỗi giây
//            }
//        }
//    }
//
//    // Hiển thị thông tin
//    NetworkUsageUI(receivedData, transmittedData, received, sent)
//}
//
//@Composable
//fun NetworkUsageUI(received: Long, transmitted: Long, apiReceived: Long, apiTransmitted: Long) {
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Data Received: ${received / (1024 * 1024)} Mb")
//        Text("Data Sent: ${transmitted / (1024 * 1024)} Mb")
//        Text("Total: ${(received + transmitted) / (1024 * 1024)} Mb")
//        Text("Data Received Call API: ${apiReceived / (1024)} Mb")
//        Text("Data Sent Call API: ${apiReceived / (1024)} Mb")
//    }
//}
