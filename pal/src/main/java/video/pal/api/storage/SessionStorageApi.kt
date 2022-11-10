package video.pal.api.storage

import video.pal.api.models.Session

interface SessionStorageApi {

    fun get(): Session?

    fun save(session: Session? = null)
}