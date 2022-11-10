package video.pal.api

import android.util.Log
import video.pal.api.http.SessionHttpApi
import video.pal.api.models.Session
import video.pal.api.models.SessionDtoReq
import video.pal.api.storage.SessionStorageApi
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