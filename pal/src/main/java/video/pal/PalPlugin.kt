package video.pal

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import video.pal.api.EventApi
import video.pal.api.SessionApi
import video.pal.api.exceptions.PalNotInitializedException
import video.pal.api.http.EventHttpApi
import video.pal.api.http.HttpClient
import video.pal.api.http.SessionHttpApi
import video.pal.api.models.PalOptions
import video.pal.api.models.PalVideoTrigger
import video.pal.api.storage.LocalSessionStorageApi
import video.pal.sdk.PalSdk


const val PAL_SERVER_URL = "https://back.pal.video"

@OptIn(DelicateCoroutinesApi::class)
class PalPlugin private constructor() {

    private lateinit var sessionApi: SessionApi

    private lateinit var eventApi: EventApi

    private lateinit var palOptions: PalOptions

    private lateinit var palSdk: PalSdk

    private var triggeredVideo: PalVideoTrigger? = null

    private val eventNotifierScope = CoroutineScope(Dispatchers.Default)

    companion object  {

        private val TAG = PalPlugin::class.java.name

        val instance: PalPlugin = PalPlugin()

        /**
         * Creates the PalPlugin instance from Context
         */
        fun setup(
            context: Context,
            apiToken: String,
            serverUrl: String? = null,
            palSdk: PalSdk? = null,
        ) {
            val retrofitClient = HttpClient.getInstance(
                serverUrl ?: PAL_SERVER_URL,
                apiToken
            )
            val sharedPreferences = context.getSharedPreferences("PAL", Context.MODE_PRIVATE)
            val sessionApi = SessionApi(
                retrofitClient.create(SessionHttpApi::class.java),
                LocalSessionStorageApi(sharedPreferences)
            )
            val eventApi = EventApi(retrofitClient.create(EventHttpApi::class.java))
            instance.sessionApi = sessionApi
            instance.eventApi = eventApi
            instance.palSdk = palSdk ?: PalSdk()
            instance.palOptions = PalOptions.from(context)
        }
    }

    @Suppress("DeferredResultUnused")
    suspend fun initialize() {
        try {
            sessionApi.initSession()
        } catch (err: java.lang.Exception) {
            Log.e(TAG, "Error while init session", err)
        }
    }

    fun logCurrentScreen(context: Context, path: String) {
        if(context is Activity) {
            return logCurrentScreen(context, path)
        }
        val activity = context.getActivity() ?: return
        return logCurrentScreen(activity, path)
    }

    fun logCurrentScreen(activity: Activity, path: String) {
        try {
            Log.d(TAG, "...logCurrentScreen on path: $path")
            GlobalScope.launch(Dispatchers.Main) {
                sessionApi.initSession()
                checkHasInit()
                triggeredVideo = eventApi.logCurrentScreen(sessionApi.getSession()!!, path).body()
                    ?: return@launch
                Log.d(TAG, "A video has triggered -> ${triggeredVideo!!.videoId}")
                when (triggeredVideo!!.flowType) {
                    PalVideoTrigger.VideoFlowType.TALK -> palSdk.showTalkVideo(
                        activity,
                        triggeredVideo!!.videoThumbUrl,
                        triggeredVideo!!.videoUrl,
                        triggeredVideo!!.videoSpeakerName,
                        triggeredVideo!!.videoSpeakerRole,
                        { onSkip() },
                        { onExpand() },
                        { onVideoEnd() },
                    )
                    PalVideoTrigger.VideoFlowType.SURVEY -> Log.d(TAG, "Not implemented yet")
                }
            }
        } catch (err: java.lang.Exception) {
            Log.e(TAG, "LogCurrentScreen failed ", err)
        }
    }

    fun clearSession() {
        try {
            sessionApi.clear()
        } catch (err: java.lang.Exception) {
            Log.e(TAG, "LogCurrentScreen failed ", err)
        }
    }

    fun hasInit(): Boolean {
        return sessionApi.hasSession()
    }

    // PRIVATES

    private fun checkHasInit() {
        if(!hasInit()) {
            throw PalNotInitializedException.missingSession()
        }
    }

    private fun onSkip() {
        try {
            eventNotifierScope.launch {
                eventApi.logVideoSkipped(sessionApi.getSession()!!, triggeredVideo!!)
                triggeredVideo = null
            }
        } catch (error: Exception) {
            Log.e(TAG, "Error while saving onSkip", error)
        }
    }

    private fun onExpand() {
        try {
            eventNotifierScope.launch {
                eventApi.logVideoExpanded(sessionApi.getSession()!!, triggeredVideo!!)
            }
        } catch (error: Exception) {
            Log.e(TAG, "Error while saving onExpand", error)
        }
    }

    private fun onVideoEnd() {
        try {
            eventNotifierScope.launch {
                eventApi.logVideoEnded(sessionApi.getSession()!!, triggeredVideo!!)
            }
        } catch (error: Exception) {
            Log.e(TAG, "Error while saving onVideoEnd", error)
        }
    }

    tailrec fun Context?.getActivity(): Activity? = this as? Activity
        ?: (this as? ContextWrapper)?.baseContext?.getActivity()

    private fun getActivity(view: View): Activity? {
        var context = view.context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

}