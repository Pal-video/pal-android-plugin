package com.plugin.pal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView

class CircleView: SurfaceView {

    private var borderWidth = 0
    private var canvasSize = 0
    private var paintBorder: Paint? = null
    private var path: Path? = null

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        // init paint
        paintBorder = Paint()
        paintBorder!!.isAntiAlias = true

        path = Path()

        // load the styled attributes and set their properties
        setBorderWidth(0)
        setBorderColor(Color.WHITE)

//        clipPath = Path()
//        clipPath!!.addCircle(710f, 330f, 250f, Path.Direction.CW)
    }

    fun setBorderWidth(borderWidth: Int) {
        this.borderWidth = borderWidth
        requestLayout()
        this.invalidate()
    }

    fun setBorderColor(borderColor: Int) {
        if (paintBorder != null) paintBorder!!.color = borderColor
        this.invalidate()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvasSize = canvas.width
        if (canvas.height < canvasSize) canvasSize = canvas.height

        // circleCenter is the x or y of the view's center
        // radius is the radius in pixels of the cirle to be drawn
        // paint contains the shader that will texture the shape
        val circleCenter = (canvasSize - borderWidth * 2) / 2
        val radius = circleCenter

        Log.d("CircleVideoView", String.format("canvas.height : %s", canvas.height))
        Log.d("CircleVideoView", String.format("canvas.width : %s", canvas.width))
        Log.d("CircleVideoView", String.format("circleCenter : %s", circleCenter))
        Log.d("CircleVideoView", String.format("radius : %s", radius))

        canvas.drawCircle(
            (circleCenter + borderWidth).toFloat(),
            (circleCenter + borderWidth).toFloat(),
            radius - borderWidth - 4.0f,
            paintBorder!!
        )
        path!!.addCircle(
            (circleCenter + borderWidth).toFloat(),
            (circleCenter + borderWidth).toFloat(),
            radius - 4.0f,
            Path.Direction.CW
        )
        canvas.clipPath(path!!)
//        canvas.clipPath(clipPath!!);
        super.dispatchDraw(canvas)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width: Int = measureWidth(widthMeasureSpec)
        val height: Int = measureHeight(heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    private fun measureWidth(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = if (specMode == MeasureSpec.EXACTLY) {
            // The parent has determined an exact size for the child.
            specSize
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            specSize
        } else {
            // The parent has not imposed any constraint on the child.
            canvasSize
        }
        return result
    }

    private fun measureHeight(measureSpecHeight: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)
        result = if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            specSize
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            specSize
        } else {
            // Measure the text (beware: ascent is a negative number)
            canvasSize
        }
        return result + 2
    }


}