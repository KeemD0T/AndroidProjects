package com.example.assignment9.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private const val BASE_URL = "https://www.omdbapi.com/?s=batman&apikey=d3b6da56"
        val omdbApiService: OmdApiService by lazy {

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OmdApiService::class.java)

        }

    }