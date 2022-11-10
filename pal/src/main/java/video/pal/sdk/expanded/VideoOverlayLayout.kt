package video.pal.sdk.expanded

import video.pal.sdk.common.CropVideoView

interface VideoOverlayLayout {

    fun setVideoPlayer(player: CropVideoView)

    fun setClose(exitFn: () -> Unit)
}