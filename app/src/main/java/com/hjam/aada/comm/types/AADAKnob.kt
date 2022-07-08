package com.hjam.aada.comm.types

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.scale
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min


class AADAKnob : AppCompatImageView {
    private val mPaintKnob: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintText: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBitmapOptions: BitmapFactory.Options = BitmapFactory.Options()
    private var mKnobBitmap: Bitmap =
        BitmapFactory.decodeResource(resources, com.hjam.aada.R.drawable.knob02s, mBitmapOptions)
    private var mDrawKnob = true
    private var mDrawText = true
    private var mTouchX = 0f
    private var mTouchY = 0f
    private var mCenterX = 0f
    private var mCenterY = 0f
    private var mSmallestDim = 1
    private var mWidthBitmap = 0f
    private var mHeightBitmap = 0f
    private var mKnob = 0f
    private var mKnobText = "123"
    private var mKnobTextWidth = 0
    private var mSaturated = false
    private var tmpRect = Rect()
    private var tmpKnob = 0f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        mBitmapOptions.inScaled = false
        init()
    }

    private fun init() {
        mPaintKnob.isDither = true
        mPaintKnob.color = -0xffba34 // alpha.r.g.b
        mPaintKnob.style = Paint.Style.FILL_AND_STROKE
        mPaintKnob.strokeJoin = Paint.Join.ROUND
        mPaintKnob.strokeCap = Paint.Cap.ROUND
        mPaintKnob.strokeWidth = 5f
        mPaintKnob.isAntiAlias = true
        mPaintKnob.setShadowLayer(10f, 0f, 0f, Color.BLACK)
        mPaintText.color = Color.BLACK
        mPaintText.style = Paint.Style.FILL
        mPaintText.setShadowLayer(10f, 0f, 0f, Color.BLACK)
        mPaintText.textSize = 36f
        mKnobBitmap =
            BitmapFactory.decodeResource(resources, com.hjam.aada.R.drawable.knob02s, mBitmapOptions)
        mWidthBitmap = mKnobBitmap.width.toFloat()
        mHeightBitmap = mKnobBitmap.height.toFloat()
        mCenterX = width / 2f
        mCenterY = height / 2f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSmallestDim = min(width, height)
        mPaintText.textSize = mSmallestDim*0.1f
        mKnobBitmap = mKnobBitmap.scale(mSmallestDim,mSmallestDim,true)
        mWidthBitmap= mKnobBitmap.width.toFloat()/2
        mHeightBitmap= mKnobBitmap.height.toFloat()/2
        mCenterX = width / 2f
        mCenterY = height / 2f
        mPaintText.getTextBounds(mKnobText,0,mKnobText.length,tmpRect)
        mKnobTextWidth = tmpRect.width()
    }

    override fun onDraw(canvas: Canvas) {
        if (mDrawKnob) {
            canvas.rotate(-1f * (mKnob - 90f), mWidthBitmap, mHeightBitmap)
            canvas.drawBitmap(mKnobBitmap, 0f, 0f, mPaintKnob)
            canvas.rotate((mKnob - 90f), mWidthBitmap, mHeightBitmap)
        }
        if (mDrawText) {
            canvas.drawText(mKnobText, mSmallestDim/2f - (mKnobTextWidth/2f), mSmallestDim * 0.8f, mPaintText)
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_UP) {
            performClick()
            mSaturated= false
            return true
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            invalidate()
            return true
        }
        if (event.action == MotionEvent.ACTION_MOVE) {
            mTouchX = event.getX(0)
            mTouchY = event.getY(0)
            tmpKnob = (atan2(mCenterY - mTouchY, mTouchX - mCenterX) * 180 / 3.1415f)
            if (tmpKnob < -45 && tmpKnob > -135){
                mSaturated = true
            }
            if (!mSaturated){
                mKnob = tmpKnob
                mKnobText =mKnob.toInt().toString()
                mPaintText.getTextBounds(mKnobText,0,mKnobText.length,tmpRect)
                mKnobTextWidth = tmpRect.width()
            }
            if (mSaturated && (abs(abs(tmpKnob) - abs(mKnob)) <5)){
                mSaturated= false
            }
            invalidate()
            return true
        }
        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
