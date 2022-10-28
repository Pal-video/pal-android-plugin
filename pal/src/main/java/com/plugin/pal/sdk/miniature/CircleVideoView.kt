package com.plugin.pal.sdk.miniature

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.SurfaceHolder
import android.view.View
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.plugin.pal.R


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

    var videoRatio: Int? = null

    var circleView: CircleView? = null

    var videoUrl: String? = null

    // Animation

    private var currentAnimator: Animator? = null

    private var shortAnimationDuration: Int = 0

    fun show(activity: Activity, videoUrl: String) {
        load(videoUrl)
        val mParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        //val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //wm.addView(view, mParams)
        activity.addContentView(this, mParams)
    }

    fun expand() {
        shortAnimationDuration = 1000
        currentAnimator?.cancel()

        // FIXME change for a non round view

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        circleView!!.getGlobalVisibleRect(startBoundsInt)
        findViewById<View>(R.id.video_container).getGlobalVisibleRect(finalBoundsInt, globalOffset)
        Log.d("CircleVideoView", String.format("globalOffset.x : %s", globalOffset.x))
        Log.d("CircleVideoView", String.format("globalOffset.y : %s", globalOffset.y))
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        circleView!!.pivotX = 0f
        circleView!!.pivotY = circleView!!.measuredHeight.toFloat()

        val scaleX = (finalBounds.width() - 80) / circleView!!.width
        val scaleY = scaleX * videoRatio!!

        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    circleView,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(ObjectAnimator.ofFloat(circleView, View.SCALE_X, 1f, scaleX))
                with(ObjectAnimator.ofFloat(circleView, View.SCALE_Y, 1f, scaleY))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }

    }

    private fun init(attrs: AttributeSet? = null) {
        inflate(context, R.layout.video_min, this)
        circleView = findViewById(R.id.min_videoView)
//        expandedVideo = findViewById(R.id.expanded_videoView)

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

    private fun load(videoUrl: String) {
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
        val onViewTouched = OnClickListener {
//            (parent as ViewGroup).removeView(this)
            expand()
        }
        circleView!!.setOnClickListener(onViewTouched)
    }

    var mOnVideoSizeChangedListener =
        MediaPlayer.OnVideoSizeChangedListener { mp, width, height ->
            setFitToFillAspectRatio(mp, width, height)
        }

    private fun setFitToFillAspectRatio(mp: MediaPlayer, videoWidth: Int, videoHeight: Int) {
        var screenWidth: Int = circleView!!.width
        var screenHeight: Int = circleView!!.height

        videoRatio = videoWidth / videoHeight
        layoutParams.width = screenWidth
        layoutParams.height = screenHeight
        if (videoWidth > videoHeight) {
            Log.d("CircleVideoView", "videoWidth > videoHeight")
            Log.d("CircleVideoView", String.format("screenWidth : %s", screenWidth))
            Log.d("CircleVideoView", String.format("screenHeight : %s", screenHeight))
            layoutParams.width = screenWidth
            layoutParams.height = screenHeight
//            layoutParams.height = screenWidth * videoHeight / videoWidth
        } else {
            Log.d("CircleVideoView", "videoWidth <= videoHeight")
            layoutParams.width = screenHeight * videoWidth / videoHeight
            layoutParams.height = screenHeight
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        player!!.setDisplay(holder);
        player!!.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//        Log.d("CircleVideoView", "On surface changed")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }
}