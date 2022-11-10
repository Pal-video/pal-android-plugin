package video.pal.api

import video.pal.api.http.EventHttpApi
import video.pal.api.models.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
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
        return withContext(dispatcher) {
            eventHttpApi.logEvent(eventContext)
        }
    }

    suspend fun logVideoSkipped(session: Session, triggeredVideo: PalVideoTrigger): Response<Unit>? {
        if(triggeredVideo.eventLogId == null) {
            return null
        }
        val event = VideoTriggerEvent(
            VideoTriggerEvent.VideoTriggerEvents.videoSkip,
            Date(),
            session.uid,
            emptyMap()
        )
        return withContext(dispatcher) {
            eventHttpApi.logTriggeredVideoEvent(triggeredVideo.eventLogId, event)
        }
    }

    suspend fun logVideoExpanded(session: Session, triggeredVideo: PalVideoTrigger): Response<Unit>? {
        if(triggeredVideo.eventLogId == null) {
            return null
        }
        val event = VideoTriggerEvent(
            VideoTriggerEvent.VideoTriggerEvents.minVideoOpen,
            Date(),
            session.uid,
            emptyMap()
        )
        return withContext(dispatcher){
            eventHttpApi.logTriggeredVideoEvent(triggeredVideo.eventLogId, event)
        }
    }

    suspend fun logVideoEnded(session: Session, triggeredVideo: PalVideoTrigger): Response<Unit>? {
        if(triggeredVideo.eventLogId == null) {
            return null
        }
        val event = VideoTriggerEvent(
            VideoTriggerEvent.VideoTriggerEvents.videoViewed,
            Date(),
            session.uid,
            emptyMap()
        )
        return withContext(dispatcher) {
            eventHttpApi.logTriggeredVideoEvent(triggeredVideo.eventLogId, event)
        }
    }

}