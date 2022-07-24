package com.hjam.aada.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.hjam.aada.R
import android.view.View
import androidx.core.graphics.scale
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ArHrz : View {
    private var mCurrentPaint: Paint? = null
    private var mWallPath = Path()
    private var mCenterX = 0f
    private var mCenterY = 0f
    private var mRoll = 0f
    private var mPitch = 0f
    private var mCanvasWidth = 0f
    private var mCanvasHeight = 0f
    private var mSmallestDim = 0f
    private var mGlassBitmap: Bitmap? = null
    private var mSkyBitmap: Bitmap? = null
    private var mSkyAspect = 1f
    private var mAHBackWidth = 0f
    private var mAHSkyWidth = 0f
    private var mAHBackHeight = 0f
    private var mAHSkyHeight = 0f
    private var mMatrix: Matrix? = null

    fun setValues(roll: Float, pitch: Float) {
        this.mRoll = roll
        this.mPitch = pitch
        invalidate()
        requestLayout()
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSmallestDim = min(w, h).toFloat()
        mGlassBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.ah_glass).scale(mSmallestDim.toInt(),mSmallestDim.toInt(),true)
        mSkyBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.horizon_sky)
        mCanvasWidth = w.toFloat()
        mCanvasHeight = h.toFloat()
        mCenterX = mCanvasWidth / 2f
        mCenterY = mCanvasHeight / 2f
        mMatrix = Matrix()
        mSkyAspect = (mSkyBitmap!!.height.toFloat() / mSkyBitmap!!.width)
        mSkyBitmap=mSkyBitmap!!.scale(
            (mSmallestDim*0.75f).toInt(),
            (mSmallestDim*0.75f*mSkyAspect).toInt()
        )
        mAHBackWidth = mGlassBitmap!!.width.toFloat()
        mAHSkyWidth = mSkyBitmap!!.width.toFloat()
        mAHBackHeight = mGlassBitmap!!.height.toFloat()
        mAHSkyHeight = mSkyBitmap!!.height.toFloat()
        mWallPath.reset()
        mWallPath.moveTo(mSmallestDim*0.95f, mSmallestDim/2);
        repeat(60) {
            mWallPath.lineTo(mSmallestDim/2 + (mSmallestDim * 0.45f * cos(it * Math.PI / 30f)).toFloat(), mSmallestDim/2 + (mSmallestDim * 0.45f * sin(it * Math.PI / 30)).toFloat())
        }
        mCurrentPaint!!.strokeWidth = mSmallestDim / 200f
        mCurrentPaint!!.color = Color.WHITE
        mCurrentPaint!!.style = Paint.Style.FILL
        mCurrentPaint!!.alpha = 255
    }

    private fun init() {
        mCurrentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCurrentPaint?.isDither = false
        mCurrentPaint?.color = Color.argb(255, 234, 28, 58) // alpha.r.g.b
        mCurrentPaint?.style = Paint.Style.STROKE
        mCurrentPaint?.strokeJoin = Paint.Join.ROUND
        mCurrentPaint?.strokeCap = Paint.Cap.ROUND
        mCurrentPaint?.strokeWidth = 5f
        mCurrentPaint?.isAntiAlias = true
        mCurrentPaint?.setShadowLayer(10f, 3f, 3f, Color.BLACK)
        mMatrix = Matrix()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mMatrix?.reset()
        mMatrix?.preRotate(mRoll, mAHBackWidth / 2.0f, mAHBackHeight / 2.0f)
        mMatrix?.preTranslate(
            mSmallestDim/2 - mAHSkyWidth / 2.0f,
            mSmallestDim/2 - mAHSkyHeight * (0.5f - 0.36f * mPitch / 90.0f)
        )
        canvas.save()
        canvas.clipPath(mWallPath)
        canvas.drawBitmap(mSkyBitmap!!, mMatrix!!, mCurrentPaint)
        canvas.restore()
        canvas.drawLine(mSmallestDim * 0.2f, mSmallestDim/2, mSmallestDim * 0.8f, mSmallestDim/2, mCurrentPaint!!)
        canvas.drawBitmap(
            mGlassBitmap!!,
            0f,
            0f,
            mCurrentPaint)
    }
}