package com.cpen321.usermanagement.data.remote.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TriviaRetrofit {
    private val client = OkHttpClient.Builder().build()

    val api: TriviaAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://the-trivia-api.com/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TriviaAPI::class.java)
    }
}