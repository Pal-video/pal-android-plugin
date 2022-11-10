package video.pal.api.models

import java.util.Date

data class PalVideoTrigger(
    val eventLogId: String?,
    val videoId: String,
    val creationDate: Date,
    val videoUrl: String,
    val videoThumbUrl: String,
    val imgThumbUrl: String,
    val videoSpeakerName: String,
    val videoSpeakerRole: String,
    val survey: Survey?
) {

    data class Survey(
        val userName: String,
        val companyTitle: String,
        val avatarUrl: String?)

    enum class VideoFlowType {
        TALK,
        SURVEY
    }

    val flowType: VideoFlowType
    get() {
        if(survey != null) {
            return VideoFlowType.SURVEY
        }
        return VideoFlowType.TALK
    }
}
