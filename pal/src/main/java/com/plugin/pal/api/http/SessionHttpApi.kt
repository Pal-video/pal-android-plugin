package com.plugin.pal.api.http

import com.plugin.pal.api.models.Session
import retrofit2.Response
import retrofit2.http.POST

interface SessionHttpApi {

    @POST("/sessions")
    suspend fun create(platform: String): Response<Session>
}