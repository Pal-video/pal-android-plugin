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
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.plugin.pal.R


class CircleVideoView :ConstraintLayout{

    enum class State {
        none,
        minified,
        expanded
    }

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
//        init()
    }

    var state: State = State.none

    // video layouts

    var circleView: CircleView? = null

    var cropedVideoView: CropVideoTextureView? = null

    // description layout

    var descriptionLayout: View? = null

    var titleTextView: TextView? = null

    var subtitleTextView: TextView? = null

    // powered by layout

    var poweredByLayout: FrameLayout? = null

    // video attrs

    var minVideoUrl: String? = null

    var expandedVideoUrl: String? = null

    // Animation

    private var currentAnimator: Animator? = null

    private var shortAnimationDuration: Int = 0

//    private fun init(attrs: AttributeSet? = null) {
//        inflate(context, R.layout.video_min, this)
//        circleView = findViewById(R.id.min_videoView)
//
//        val holder: SurfaceHolder = circleView!!.holder
//        holder.setFormat(PixelFormat.TRANSLUCENT)
//        holder.addCallback(this)
//
//        val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleVideoView)
//        try {
//            videoUrl = ta.getString(R.styleable.CircleVideoView_videoUrl)
//            if (videoUrl != null) {
//                player = MediaPlayer.create(
//                    this.context,
//                    Uri.parse(videoUrl)
//                )
//                player!!.isLooping = true
//                player!!.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
//            }
//        } finally {
//            ta.recycle()
//        }
//    }

    fun show(
        activity: Activity,
        minVideoUrl: String,
        expandedVideoUrl: String,
        title: String,
        subtitle: String
    ) {
        load(minVideoUrl, expandedVideoUrl, title, subtitle)
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
        state = State.minified
    }

    private fun load(
        minVideoUrl: String,
        expandedVideoUrl: String,
        title: String,
        subtitle: String
    ) {
        this.minVideoUrl = minVideoUrl
        this.expandedVideoUrl = expandedVideoUrl
        inflate(context, R.layout.video_min, this)
        circleView = findViewById(R.id.min_videoView)
        cropedVideoView = findViewById(R.id.crop_video_texture)

        descriptionLayout = findViewById(R.id.description_layout)
        titleTextView = findViewById(R.id.title)
        subtitleTextView = findViewById(R.id.subtitle)
        descriptionLayout!!.visibility = GONE

        poweredByLayout = findViewById(R.id.powered_by_layout)
        poweredByLayout!!.visibility = GONE


        titleTextView!!.text = title
        subtitleTextView!!.text = subtitle

        cropedVideoView!!.setDataSource(minVideoUrl)
        cropedVideoView!!.setScaleType(CropVideoTextureView.ScaleType.CENTER_CROP);

        val onViewTouched = OnClickListener {
//            (parent as ViewGroup).removeView(this)
            if(state == State.minified) {
                expand()
            }
        }
        circleView!!.setOnClickListener(onViewTouched)
        cropedVideoView!!.play()
    }

    fun expand() {
        cropedVideoView!!.pause()
        cropedVideoView!!.setDataSource(expandedVideoUrl)
        cropedVideoView!!.play()

        // animate

        shortAnimationDuration = 1000
        currentAnimator?.cancel()

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        circleView!!.getGlobalVisibleRect(startBoundsInt)
        findViewById<View>(R.id.video_container).getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        val finalWidth = finalBounds.width() - 180
        val finalHeight = finalBounds.height() - 120

        circleView!!.animateShape()
        circleView!!.changeRatioAnimated(finalWidth, finalHeight) {
            circleView!!.scaledAnimated(finalWidth, finalHeight)
            state = State.expanded
        }

        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    circleView,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(ObjectAnimator.ofFloat(circleView, View.TRANSLATION_X, 1f, 0f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null

//                    descriptionLayout!!.visibility = VISIBLE
//                    poweredByLayout!!.visibility = VISIBLE
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }

    }





}