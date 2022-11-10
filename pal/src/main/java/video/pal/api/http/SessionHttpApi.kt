package video.pal.api.http

import video.pal.api.models.Session
import video.pal.api.models.SessionDtoReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SessionHttpApi {

    @POST("/sessions")
    @Headers("Content-Type: application/json")
    suspend fun create(@Body sessionDto: SessionDtoReq): Response<Session>
}