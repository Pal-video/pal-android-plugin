package com.plugin.pal.api

import com.plugin.pal.api.exceptions.EventApiException
import com.plugin.pal.api.http.EventHttpApi
import com.plugin.pal.api.http.HttpClient
import com.plugin.pal.api.models.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class EventApi(
    private val eventHttpApi: EventHttpApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    suspend fun logCurrentScreen(session: Session, path: String): Response<PalVideoTrigger> {
        val eventContext = EventContext(
            session.uid,
            path,
            PalEvents.setScreen)
        return eventHttpApi.logEvent(eventContext)
    }

    suspend fun logVideoSkipped(session: Session, triggeredVideo: PalVideoTrigger): Response<PalVideoTrigger> {
        if(triggeredVideo.eventLogId == null) {
            throw EventApiException.incorrectVideo()
        }
        val event = VideoTriggerEvent(
            VideoTriggerEvent.VideoTriggerEvents.videoSkip,
            Date(),
            session.uid)
        return eventHttpApi.logTriggeredVideoEvent(triggeredVideo.eventLogId, event)
    }

    suspend fun logVideoExpanded(session: Session, triggeredVideo: PalVideoTrigger): Response<PalVideoTrigger> {
        if(triggeredVideo.eventLogId == null) {
            throw EventApiException.incorrectVideo()
        }
        val event = VideoTriggerEvent(
            VideoTriggerEvent.VideoTriggerEvents.minVideoOpen,
            Date(),
            session.uid)
        return eventHttpApi.logTriggeredVideoEvent(triggeredVideo.eventLogId, event)
    }

    suspend fun logVideoEnded(session: Session, triggeredVideo: PalVideoTrigger): Response<PalVideoTrigger> {
        if(triggeredVideo.eventLogId == null) {
            throw EventApiException.incorrectVideo()
        }
        val event = VideoTriggerEvent(
            VideoTriggerEvent.VideoTriggerEvents.videoViewed,
            Date(),
            session.uid)
        return eventHttpApi.logTriggeredVideoEvent(triggeredVideo.eventLogId, event)
    }

}