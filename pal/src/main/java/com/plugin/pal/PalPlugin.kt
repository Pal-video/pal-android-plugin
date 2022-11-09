package com.plugin.pal

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.plugin.pal.api.EventApi
import com.plugin.pal.api.SessionApi
import com.plugin.pal.api.exceptions.PalNotInitializedException
import com.plugin.pal.api.http.EventHttpApi
import com.plugin.pal.api.http.HttpClient
import com.plugin.pal.api.http.SessionHttpApi
import com.plugin.pal.api.models.PalOptions
import com.plugin.pal.api.models.PalVideoTrigger
import com.plugin.pal.api.storage.LocalSessionStorageApi
import com.plugin.pal.sdk.PalSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val PAL_SERVER_URL = "https://back.pal.video"

class PalPlugin private constructor() {

    private lateinit var sessionApi: SessionApi

    private lateinit var eventApi: EventApi

    private lateinit var palOptions: PalOptions

    private lateinit var palSdk: PalSdk

    private var triggeredVideo: PalVideoTrigger? = null

    private val backgroundScope = CoroutineScope(Dispatchers.IO)

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

    suspend fun logCurrentScreen(fragment: Fragment, path: String) {
        return logCurrentScreen(fragment.activity as Activity, path)
    }

    suspend fun logCurrentScreen(activity: Activity, path: String) {
        try {
            checkHasInit()
            Log.d(TAG, "...logCurrentScreen on path: $path")
            triggeredVideo = eventApi.logCurrentScreen(sessionApi.getSession()!!, path).body()
                ?: return
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
            backgroundScope.launch {
                triggeredVideo = null
                eventApi.logVideoSkipped(sessionApi.getSession()!!, triggeredVideo!!)
            }
        } catch (error: Exception) {
            Log.e(TAG, "Error while saving onSkip", error)
        }
    }

    private fun onExpand() {
        try {
            backgroundScope.launch {
                eventApi.logVideoExpanded(sessionApi.getSession()!!, triggeredVideo!!)
            }
        } catch (error: Exception) {
            Log.e(TAG, "Error while saving onExpand", error)
        }
    }

    private fun onVideoEnd() {
        try {
            backgroundScope.launch {
                eventApi.logVideoEnded(sessionApi.getSession()!!, triggeredVideo!!)
            }
        } catch (error: Exception) {
            Log.e(TAG, "Error while saving onVideoEnd", error)
        }
    }

}