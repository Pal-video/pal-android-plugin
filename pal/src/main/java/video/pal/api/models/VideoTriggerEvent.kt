package video.pal.api.models

import java.util.*

data class VideoTriggerEvent(
    val type: VideoTriggerEvents,
    val time: Date,
    val sessionId: String,
    val args: Map<String, String>
) {
    enum class VideoTriggerEvents {
        videoSkip, minVideoOpen, videoViewed, answer
    }
}