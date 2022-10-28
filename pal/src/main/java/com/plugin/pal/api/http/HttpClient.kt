package com.plugin.pal.api.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpClient {
    val prodUrl = "https://back.pal.video"

    fun getInstance(baseUrl: String? = null): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl ?: prodUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}