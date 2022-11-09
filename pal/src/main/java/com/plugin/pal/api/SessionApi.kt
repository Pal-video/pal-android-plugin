package com.plugin.pal.api

import android.util.Log
import com.plugin.pal.PalPlugin
import com.plugin.pal.api.http.SessionHttpApi
import com.plugin.pal.api.models.Session
import com.plugin.pal.api.models.SessionDtoReq
import com.plugin.pal.api.storage.SessionStorageApi
import kotlinx.coroutines.*

class SessionApi(
    private val sessionHttpApi: SessionHttpApi,
    private val sessionStorageApi: SessionStorageApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    companion object {
        val TAG = SessionApi::javaClass.name
    }

    private var session: Session? = null

    suspend fun initSession()  {
        Log.d(TAG, "...Init session")
        session = sessionStorageApi.get()
        if(session != null) {
            Log.e(TAG, "A session already exists - skip")
            return
        }
        return withContext(dispatcher) {
            val sessionResult =  sessionHttpApi.create(SessionDtoReq("android", "FLUTTER")) // FIXME change to ANDROID
            session = sessionResult.body()
            Log.d(TAG, "session created with uid ${session?.uid}")
            sessionStorageApi.save(session)
            Log.d(TAG, "session saved in local storage")
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