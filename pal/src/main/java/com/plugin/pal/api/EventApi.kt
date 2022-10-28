package com.plugin.pal.api

import com.plugin.pal.api.http.EventHttpApi
import com.plugin.pal.api.http.HttpClient
import com.plugin.pal.api.models.EventContext
import com.plugin.pal.api.models.PalEvents
import com.plugin.pal.api.models.Session
import retrofit2.Response
import retrofit2.Retrofit

class EventApi(val retrofit: Retrofit) {

    val eventHttpApi = HttpClient.getInstance(null).create(EventHttpApi::class.java)

    suspend fun logCurrentScreen(session: Session, path: String): Response<Session> {
        val eventContext = EventContext(
            session.uid,
            path,
            PalEvents.setScreen)
        return eventHttpApi.logEvent(eventContext)
    }

}