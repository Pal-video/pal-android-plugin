package com.plugin.pal.sdk.expanded

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.plugin.pal.R
import com.plugin.pal.sdk.common.CropVideoView


class ExpandedVideoView: ConstraintLayout, CropVideoView.MediaPlayerListener  {

    enum class ExpandedState {
        STARTING,
        EXPANDED,
        ENDED
    }

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

    var state: ExpandedState? = null

    // video layouts

    var cropedVideoView: CropVideoView? = null

    var videoContainer: FrameLayout? = null

    var videoOverlay: View? = null

    // video attrs

    var expandedVideoUrl: String? = null

    fun init() {
        inflate(context, R.layout.expanded_video_layout, this)
        cropedVideoView = findViewById(R.id.crop_video_texture)
        videoContainer = findViewById(R.id.video_container)
        videoContainer!!.alpha = 0f
        videoContainer!!.visibility = VISIBLE

        this.state = ExpandedState.STARTING
    }

    fun show(activity: Activity) {
        val mParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        activity.addContentView(this, mParams)
        Handler(activity.mainLooper).postDelayed({
            handleVideoRatio()
            enterAnimated()
        }, 50)
    }

    fun play(videoUrl: String) {
        expandedVideoUrl = videoUrl
        cropedVideoView!!.setLooping(false)
        cropedVideoView!!.setScaleType(CropVideoView.ScaleType.CENTER_CROP);
        cropedVideoView!!.setListener(this)
        cropedVideoView!!.setDataSource(expandedVideoUrl)
        cropedVideoView!!.play()
    }

    fun setLayout(overlay: View) {
        this.videoOverlay = overlay
        if(overlay is VideoOverlayLayout) {
            overlay.setVideoPlayer(cropedVideoView!!)
        }
        videoContainer!!.addView(videoOverlay)
    }

    private fun handleVideoRatio() {
        val ratio = 16/9f
        val width = videoContainer!!.measuredWidth
        val height = videoContainer!!.measuredHeight
        val margin = 72
        videoContainer!!.layoutParams.height =  ((width - margin) * ratio).toInt()
        videoContainer!!.layoutParams.width = (width - margin)
        videoContainer!!.translationX = -margin.toFloat() / 2

        videoContainer!!.requestLayout()
    }

    private var enterAnim: AnimatorSet? = null

    private fun enterAnimated() {
        if(state != ExpandedState.STARTING || videoContainer!!.height == 0) {
            return
        }
        state = ExpandedState.EXPANDED
        val view = videoContainer
        videoContainer!!.pivotX = 0f
        videoContainer!!.pivotY = view!!.measuredHeight.toFloat()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            videoContainer!!.resetPivot()
        }
        enterAnim = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, videoContainer!!.height.toFloat(), 0f),
            )
            duration = 600
            interpolator = LinearInterpolator()
            start()
        }
    }

    private fun exitAnimated() {
        if(state != ExpandedState.EXPANDED || videoContainer!!.height == 0) {
            return
        }
        state = ExpandedState.ENDED
        val videoContainer = videoContainer
        val view = this
        this.videoContainer!!.pivotX = 0f
        this.videoContainer!!.pivotY = videoContainer!!.measuredHeight.toFloat()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            this.videoContainer!!.resetPivot()
        }
        enterAnim = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(videoContainer, View.ALPHA, 1f, 0f),
                ObjectAnimator.ofFloat(videoContainer, View.TRANSLATION_Y, 1f, videoContainer.height.toFloat()),
            )
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    Handler(view.context.mainLooper).postDelayed({
                        (view.parent as ViewGroup).removeView(view)
                    }, 500)
                }
            })
            duration = 600
            interpolator = LinearInterpolator()
            start()
        }
    }

    override fun onVideoPrepared() {}

    override fun onVideoEnd() {
        Log.d("ExpandedVideoView", "on video end")
        exitAnimated()
    }


}