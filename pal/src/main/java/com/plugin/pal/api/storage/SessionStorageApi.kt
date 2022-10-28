package com.plugin.pal.api.storage

import com.plugin.pal.api.models.Session

interface SessionStorageApi {

    fun get(): Session?

    fun save(session: Session? = null)
}