package video.pal.api.exceptions

class PalNotInitializedException(message: String): Exception(message) {

    companion object {
        fun missingSession(): PalNotInitializedException {
            return PalNotInitializedException("Pal requires a session for this action. Please ensure you properly called initialize before doing this.")
        }
    }
}