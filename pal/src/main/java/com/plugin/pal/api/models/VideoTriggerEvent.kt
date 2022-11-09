package com.plugin.pal.api.models

import java.util.*

data class VideoTriggerEvent(
    val type: VideoTriggerEvents,
    val time: Date,
    val sessionId: String,
    //val attrs: Map<String, String>? = null
) {
    enum class VideoTriggerEvents {
        videoSkip, minVideoOpen, videoViewed, answer
    }
}