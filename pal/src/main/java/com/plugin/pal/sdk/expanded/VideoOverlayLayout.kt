package com.plugin.pal.sdk.expanded

import android.view.View
import com.plugin.pal.sdk.common.CropVideoView

interface VideoOverlayLayout {

    fun setVideoPlayer(player: CropVideoView)

    fun setClose(exitFn: () -> Unit)
}