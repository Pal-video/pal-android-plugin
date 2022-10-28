package com.plugin.pal.api.http

import com.plugin.pal.api.models.EventContext
import com.plugin.pal.api.models.Session
import retrofit2.Response
import retrofit2.http.POST

interface EventHttpApi {

    @POST("/eventlogs")
    suspend fun logEvent(eventContext: EventContext): Response<Session>
}