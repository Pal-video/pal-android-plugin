package com.plugin.pal.api

import android.util.Log
import com.plugin.pal.api.http.SessionHttpApi
import com.plugin.pal.api.models.Session
import com.plugin.pal.api.storage.SessionStorageApi
import kotlinx.coroutines.*

class SessionApi(
    private val sessionHttpApi: SessionHttpApi,
    private val sessionStorageApi: SessionStorageApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private var session: Session? = null

    suspend  fun initSession()  {
        session = sessionStorageApi.get()
        if(session != null) {
            return
        }
        withContext(dispatcher) {
            val sessionResult =  sessionHttpApi.create("android")
            session = sessionResult.body()
            sessionStorageApi.save(session)
        }
    }

    fun hasSession(): Boolean {
        return session != null
    }

    fun getSession(): Session? {
        return session
    }

    fun clear() {
        session = null
        sessionStorageApi.save()
    }

}