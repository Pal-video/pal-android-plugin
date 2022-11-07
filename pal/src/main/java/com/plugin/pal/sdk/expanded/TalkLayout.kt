package com.plugin.pal.sdk.expanded

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import com.plugin.pal.R
import com.plugin.pal.sdk.common.CropVideoView

class TalkLayout:ConstraintLayout, VideoOverlayLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    // layout attributes

    var descriptionLayout: View? = null

    var titleTextView: TextView? = null

    var subtitleTextView: TextView? = null

    var poweredByLayout: ConstraintLayout? = null

    var videoView: VideoView? = null

    var time: TextView? = null

    private var mVideoPlayer: CropVideoView? = null

    fun init() {
        inflate(context, R.layout.talk_video_layout, this)
        descriptionLayout = findViewById(R.id.description_layout)
        titleTextView = findViewById(R.id.title)
        subtitleTextView = findViewById(R.id.subtitle)
        time = findViewById(R.id.timerTextView)
        poweredByLayout = findViewById(R.id.powered_by_layout)

        time!!.text = "00:00"
    }

    fun initData(
        title: String,
        subtitle: String,
    ) {
        titleTextView!!.text = title
        subtitleTextView!!.text = subtitle
    }

    override fun setVideoPlayer(player: CropVideoView) {
        mVideoPlayer = player
        mVideoPlayer!!.setTimeListener {
            videoTime -> time!!.text = videoTime.toString()
        }
    }


}