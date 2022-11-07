package com.plugin.pal.sdk.expanded.widgets

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout

class RoundedVideoContainer : FrameLayout  {

    constructor(context: Context): super(context) {}

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        mRadius = attrs.getAttributeFloatValue(null, "corner_radius", 0f)
    }

    var mRadius: Float = 40f
        get() = field

        set(value) {
            field = value
        }

    private val mPath: Path = Path()

    private val mRect = RectF()

    fun enterAnimated() {
        visibility = VISIBLE
        val view = this
        this.pivotX = measuredWidth.toFloat() / 2
        this.pivotY = measuredHeight.toFloat() / 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            resetPivot()
        }
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f),
            )
            startDelay = 500
            duration = 700
            interpolator = OvershootInterpolator()
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val savedState: Int = canvas.save()
        val w = width.toFloat()
        val h = height.toFloat()
        mPath.reset()
        mRect[0f, 0f, w] = h
        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CCW)
        mPath.close()
        canvas.clipPath(mPath)
        super.onDraw(canvas)
        canvas.restoreToCount(savedState)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        computePath(w, h)
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save: Int = canvas.save()
        computePath(measuredWidth, measuredHeight)
        canvas.clipPath(mPath)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }

    private fun computePath(w: Int, h: Int) {
        mPath.reset()
        val centerX = w.toFloat()
        val centerY = h.toFloat()
        mPath.addRoundRect(
            centerX, centerY, 0f, 0f,
            mRadius,
            mRadius,
            Path.Direction.CCW)
        mPath.close()
    }

}