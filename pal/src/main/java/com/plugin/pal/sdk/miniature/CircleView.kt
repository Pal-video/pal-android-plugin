package com.plugin.pal.sdk.miniature

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import kotlin.math.min


class CircleView : FrameLayout {

    constructor(context: Context): super(context) {}

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        mRadius = attrs.getAttributeFloatValue(null, "corner_radius", 0f)
    }

    var mRadius: Float = 250f
        get() = field

        set(value) {
            field = value
            Log.d("CircleView", "--> $value")
        }

    private val mPath: Path = Path()

    private val mRect = RectF()

    fun animateShape() {
        val animator = ValueAnimator.ofFloat(mRadius, 12f).apply {
            duration = 500
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                mRadius = valueAnimator.animatedValue as Float
                invalidate()
            }
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        val savedState: Int = canvas.save()
        val w = width.toFloat()
        val h = height.toFloat()
        mPath.reset()
        mRect[0f, 0f, w] = h
        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CCW)
        mPath.close()
        val debug: Boolean = canvas.clipPath(mPath)
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