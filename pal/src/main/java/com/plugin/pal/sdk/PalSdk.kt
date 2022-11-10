package com.plugin.pal.sdk

import android.app.Activity
import android.view.View
import com.plugin.pal.sdk.expanded.ExpandedVideoView
import com.plugin.pal.sdk.expanded.TalkLayout
import com.plugin.pal.sdk.miniature.MinVideoLayout

class PalSdk {

    /**
     * Shows a talk video as a popup above an activity
     */
    fun showTalkVideo(
        activity: Activity,
        minVideoUrl: String,
        expandedVideoUrl: String,
        userName: String,
        companyTitle: String,
        onSkip: () -> Unit,
        onExpand: () -> Unit,
        onVideoEnd: () -> Unit,
    ) {
        val talkLayout = TalkLayout(activity)
        talkLayout.setDescription(userName, companyTitle)
        talkLayout.setOnCloseTapListener(onSkip)

        val onVideoExpand = {
            onExpand()
            showExpandedVideo(
                activity,
                expandedVideoUrl,
                talkLayout,
                onVideoEnd)
        }
        showMinVideo(
            activity,
            minVideoUrl,
            onSkip,
            onVideoExpand
        )
    }

    // ------------------------------------------------
    // PRIVATES
    // ------------------------------------------------

    /**
     * Shows a circular min video
     */
    private fun showMinVideo(
        activity: Activity,
        minVideoUrl: String,
        onSkip: () -> Unit,
        onExpand: () -> Unit
    ) {
        val minVideoView = MinVideoLayout(activity)
        minVideoView.init(minVideoUrl, onExpand)
        minVideoView.show(activity)
    }

    /**
     * Shows an expanded video
     */
    private fun showExpandedVideo(
        activity: Activity,
        expandedVideoUrl: String,
        expandedLayout: View,
        onVideoEnd: () -> Unit,
    ) {
        val expandedVideo = ExpandedVideoView(activity)

        expandedVideo.show(activity)
        expandedVideo.setLayout(expandedLayout)
        expandedVideo.onVideoEndListener = onVideoEnd
        expandedVideo.play(expandedVideoUrl)
    }

}