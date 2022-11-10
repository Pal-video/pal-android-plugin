package video.pal.sdk.miniature.widgets

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout


class CircleVideoView : FrameLayout {

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
            duration = 150L
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                mRadius = valueAnimator.animatedValue as Float
                requestLayout()
            }
        }
        animator.start()
    }

    fun changeRatioAnimated(
        finalWidth: Float,
        finalHeight: Float,
    ) {
        val ratio = (finalWidth / finalHeight)
        val initialWidth = measuredWidth
        val initialHeight = measuredHeight
        val animator = ValueAnimator.ofFloat(1f, ratio).apply {
            duration = 150L
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                layoutParams.width = (value * initialWidth).toInt()
//                layoutParams.height = (value * initialHeight).toInt()
                requestLayout()
            }
          addListener(object: Animator.AnimatorListener {
              override fun onAnimationStart(animation: Animator) {}
              override fun onAnimationEnd(animation: Animator) {}
              override fun onAnimationCancel(animation: Animator) {}
              override fun onAnimationRepeat(animation: Animator) {}
          })
        }
        animator.start()
    }

    fun scaledAnimated(finalWidth: Float, finalHeight: Float, onAnimationEnd: () -> Unit) {
        val view = this
        val scaleX = finalWidth / width
        val scaleY = finalHeight / height
        val startX = translationX;
        val startY = translationY;

        this.pivotX = 0f
        this.pivotY = measuredHeight.toFloat()
        var animator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, scaleX),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, scaleY),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, startX, -convertDpToPixel(24f)),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startY, convertDpToPixel(24f)),
            )
            duration = 300L
            interpolator = LinearInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onAnimationEnd()
                }
                override fun onAnimationCancel(animation: Animator) {}
            })
            start()
        }
    }

    fun enterAnimated(onAnimationEnd: (() -> Unit)? = null) {
        val view = this
        this.alpha = 0f
        this.pivotX = measuredWidth.toFloat() / 2
        this.pivotY = measuredHeight.toFloat() / 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            resetPivot()
        }
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f),
            )
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if(onAnimationEnd != null) {
                        onAnimationEnd()
                    }
                }
            })
            startDelay = 1500
            duration = 500
            interpolator = OvershootInterpolator()
            start()
        }
    }

    fun exitAnimated(onAnimationEnd: () -> Unit) {
        val view = this
        this.pivotX = measuredWidth.toFloat() / 2
        this.pivotY = measuredHeight.toFloat() / 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            resetPivot()
        }
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X,  1f, 0f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f),
            )
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    onAnimationEnd()
                }
            })
            startDelay = 100
            duration = 300
            interpolator = AccelerateInterpolator()
            start()
        }
    }

    private fun convertDpToPixel(dp: Float): Float {
        val density = context.resources.displayMetrics.densityDpi;
        return (dp * (density/ 160.0f))
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