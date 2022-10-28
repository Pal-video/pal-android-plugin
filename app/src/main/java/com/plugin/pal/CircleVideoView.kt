package com.plugin.pal

import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import androidx.constraintlayout.widget.ConstraintLayout

//class CircleVideoView: SurfaceHolder.Callback {
class CircleVideoView :ConstraintLayout, SurfaceHolder.Callback {

    constructor(context: Context): super(context) {
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    var player: MediaPlayer? = null

    var circleView: CircleView? = null

    var videoUrl: String? = null

    fun load(videoUrl: String) {
        this.videoUrl = videoUrl
        inflate(context, R.layout.video_min, this)
        circleView = findViewById(R.id.min_videoView)

        val holder: SurfaceHolder = circleView!!.holder
        holder.setFormat(PixelFormat.TRANSLUCENT)
        holder.addCallback(this)
        player = MediaPlayer.create(
            this.context,
            Uri.parse(videoUrl)
        )
        player!!.isLooping = true
        player!!.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
    }

    private fun init(attrs: AttributeSet? = null) {
        inflate(context, R.layout.video_min, this)
        circleView = findViewById(R.id.min_videoView)

        val holder: SurfaceHolder = circleView!!.holder
        holder.setFormat(PixelFormat.TRANSLUCENT)
        holder.addCallback(this)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleVideoView)
        try {
            videoUrl = ta.getString(R.styleable.CircleVideoView_videoUrl)
            if(videoUrl != null ) {
                player = MediaPlayer.create(
                    this.context,
                    Uri.parse(videoUrl)
                )
                player!!.isLooping = true
                player!!.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            }
        } finally {
            ta.recycle()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val videoWith = player!!.videoWidth
        val videoHeight = player!!.videoHeight
        val ratio = videoWith / videoHeight
        Log.d("CircleVideoView", String.format("videoWith : %s", videoWith))
        Log.d("CircleVideoView", String.format("videoHeight : %s", videoHeight))
        Log.d("CircleVideoView", String.format("ratio : %s", ratio))

        player!!.setDisplay(holder);
        player!!.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    var mOnVideoSizeChangedListener =
        MediaPlayer.OnVideoSizeChangedListener { mp, width, height ->
            setFitToFillAspectRatio(mp, width, height)
        }

    private fun setFitToFillAspectRatio(mp: MediaPlayer, videoWidth: Int, videoHeight: Int) {
        val screenWidth: Int = circleView!!.width
        val screenHeight: Int = circleView!!.height
        val videoWith = player!!.videoWidth
        val videoHeight = player!!.videoHeight
        val ratio = videoWith / videoHeight
        if (videoWidth > videoHeight) {
            Log.d("CircleVideoView", "videoWidth > videoHeight")
            Log.d("CircleVideoView", String.format("screenWidth : %s", screenWidth))
            Log.d("CircleVideoView", String.format("screenHeight : %s", screenHeight))
            layoutParams.width = screenWidth
            layoutParams.height = screenWidth * videoHeight / videoWidth
        } else {
            Log.d("CircleVideoView", "videoWidth <= videoHeight")
            layoutParams.width = screenHeight * videoWidth / videoHeight
            layoutParams.height = screenHeight
        }
    }
}