package com.rangganf.githubuserbangkit.data.remote

import com.rangganf.githubuserbangkit.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {

    // Nggawe OkHttpClient kanggo nggawe requess e HTTP.
    private val okhttp = OkHttpClient.Builder()
        .apply {
            // Nggawe interceptor logging HTTP kanggo tujuan debugging.
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    // Nggawe Kayata Retrofit kanggo nangani panjalukan API.
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okhttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Nggawe conto antarmuka ApiService kanggo hubungi utowo nyeluk API.
    val apiService = retrofit.create<ApiService>()
}

