package com.plugin.pal.sdk.miniature

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.plugin.pal.R
import com.plugin.pal.sdk.common.CropVideoView
import com.plugin.pal.sdk.miniature.widgets.CircleVideoView


class MinVideoLayout :ConstraintLayout{

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
    }

    // video layouts

    var circleView: CircleVideoView? = null

    var cropedVideoView: CropVideoView? = null

    var onExpand: (() -> Unit)? = null

    // video attrs

    var minVideoUrl: String? = null


    fun show(activity: Activity) {
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
        circleView!!.enterAnimated()
    }

    fun init(
        minVideoUrl: String,
        onExpand: () -> Unit
    ) {
        this.minVideoUrl = minVideoUrl
        this.onExpand = onExpand
//        this.expandedView = expandedView

        inflate(context, R.layout.video_min, this)
        circleView = findViewById(R.id.min_videoView)
        cropedVideoView = findViewById(R.id.crop_video_texture)

        cropedVideoView!!.setDataSource(minVideoUrl)
        cropedVideoView!!.setLooping(true)
        cropedVideoView!!.setScaleType(CropVideoView.ScaleType.CENTER_CROP);

        circleView!!.visibility = GONE

        val onViewTouched = OnClickListener {
            circleView!!.exitAnimated {
                onExpand()
            }
        }
        circleView!!.setOnClickListener(onViewTouched)
        cropedVideoView!!.play()
    }

}