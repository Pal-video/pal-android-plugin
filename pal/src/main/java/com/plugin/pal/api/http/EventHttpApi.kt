package com.plugin.pal.api.http

import com.plugin.pal.api.models.EventContext
import com.plugin.pal.api.models.PalVideoTrigger
import com.plugin.pal.api.models.Session
import com.plugin.pal.api.models.VideoTriggerEvent
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface EventHttpApi {

    @Headers("Content-Type: application/json")
    @POST("/eventlogs")
    suspend fun logEvent(@Body eventContext: EventContext): Response<PalVideoTrigger>

    @Headers("Content-Type: application/json")
    @POST("/eventlogs/{id}")
    suspend fun logTriggeredVideoEvent(
        @Path("id") triggeredVideoID: String,
        @Body event: VideoTriggerEvent
    ): Response<Unit>
}