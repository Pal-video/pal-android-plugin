package com.plugin.pal.sdk

import android.app.Activity
import com.plugin.pal.sdk.miniature.CircleVideoView

class PalSdk {

    fun showVideo(
        activity: Activity,
        minVideoUrl: String,
        expandedVideoUrl: String,
        userName: String,
        companyTitle: String,
        onSkip: () -> Unit,
        onExpand: () -> Unit,
        onVideoEnd: () -> Unit,
    ) {
        val minVideoView = CircleVideoView(activity)
        minVideoView.show(
            activity,
            minVideoUrl, expandedVideoUrl,
            userName, companyTitle
        )
    }

}