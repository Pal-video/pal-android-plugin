package video.pal.api.http

import video.pal.api.models.EventContext
import video.pal.api.models.PalVideoTrigger
import video.pal.api.models.VideoTriggerEvent
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