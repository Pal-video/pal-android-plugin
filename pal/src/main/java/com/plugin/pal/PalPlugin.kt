package com.plugin.pal

import android.content.Context
import com.plugin.pal.api.SessionApi
import com.plugin.pal.api.exceptions.PalNotInitializedException
import com.plugin.pal.api.http.HttpClient
import com.plugin.pal.api.http.SessionHttpApi
import com.plugin.pal.api.models.PalOptions
import com.plugin.pal.api.storage.LocalSessionStorageApi

class PalPlugin(
    private val sessionApi: SessionApi,
    private val palOptions: PalOptions
) {

    companion object  {

        /**
         * Creates the PalPlugin instance from Context
         */
        fun fromContext(
            context: Context
        ) : PalPlugin {
            val sharedPreferences = context.getSharedPreferences("PAL", Context.MODE_PRIVATE)
            val sessionApi = SessionApi(
                HttpClient.getInstance().create(SessionHttpApi::class.java),
                LocalSessionStorageApi(sharedPreferences)
            )
            return PalPlugin(
                sessionApi,
                PalOptions.from(context)
            )
        }

    }

    @Suppress("DeferredResultUnused")
    suspend fun initialize() {
        sessionApi.initSession()
    }

    fun logCurrentScreen(path: String) {
        checkHasInit()
        // screenTriggeredVideo = eventApi.logScreen(path)
        //        if (screenTriggeredVideo == null) {
        //            return;
        //        }


        //        if (screenTriggeredVideo.isTalkType) {
        //            await _showVideo(
        //                    context: buildContext,
        //            trigger: screenTriggeredVideo,
        //            );
        //        } else if (screenTriggeredVideo.isSurveyType) {
        //            await _showSurvey(
        //                    context: buildContext,
        //            trigger: screenTriggeredVideo,
        //            );
        //        }
    }

    fun hasInit(): Boolean {
        return sessionApi.hasSession()
    }

    private fun checkHasInit() {
        if(!hasInit()) {
            throw PalNotInitializedException.missingSession()
        }
    }

}