package com.plugin.pal.sdk.miniature

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout


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
        }

    private val mPath: Path = Path()

    private val mRect = RectF()

    fun animateShape() {
        val animator = ValueAnimator.ofFloat(mRadius, 12f).apply {
            duration = 500
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                mRadius = valueAnimator.animatedValue as Float
                requestLayout()
            }
        }
        animator.start()
    }

    fun changeRatioAnimated(finalWidth: Float,
                            finalHeight: Float,
                            onAnimationEnd: () -> Unit,
    ) {
        val ratio = (finalWidth / finalHeight)
        val initialWidth = measuredWidth
        val initialHeight = measuredHeight
        val animator = ValueAnimator.ofFloat(1f, ratio).apply {
            duration = 500
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                layoutParams.width = (value * initialWidth).toInt()
//                layoutParams.height = (value * initialHeight).toInt()
                requestLayout()
            }
          addListener(object: Animator.AnimatorListener {
              override fun onAnimationStart(animation: Animator) {}
              override fun onAnimationEnd(animation: Animator) {
                  onAnimationEnd()
              }
              override fun onAnimationCancel(animation: Animator) {}
              override fun onAnimationRepeat(animation: Animator) {}
          })
        }
//
        animator.start()
    }

    fun scaledAnimated(finalWidth: Float, finalHeight: Float) {
        val scaleX = finalWidth / width
        val scaleY = finalHeight / height
        val anim: Animation = ScaleAnimation(
            1f, scaleX,
            1f, scaleY,
            Animation.RELATIVE_TO_SELF, 0f, // Pivot point X
            Animation.RELATIVE_TO_SELF, 1f  // Pivot point Y
        )
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 1000
//        anim.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation?) {}
//            override fun onAnimationEnd(animation: Animation?) {}
//            override fun onAnimationRepeat(animation: Animation?) {}
//        })
        startAnimation(anim)
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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