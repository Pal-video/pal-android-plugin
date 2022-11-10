package video.pal.api.exceptions

class EventApiException(message: String): Exception(message) {

    companion object {
        fun incorrectVideo(): EventApiException {
            return EventApiException("Pal requires a video triggered with a valid eventLog uid")
        }
    }
}