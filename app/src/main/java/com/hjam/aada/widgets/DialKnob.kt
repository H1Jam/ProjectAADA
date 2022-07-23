package com.hjam.aada.widgets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.scale
import com.hjam.aada.R
import com.hjam.aada.utils.Helpers
import kotlin.math.*


class DialKnob : AppCompatImageView {
    private val mPaintKnob: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintText: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintRoller: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBitmapOptions: BitmapFactory.Options = BitmapFactory.Options()
    private var mKnobBitmap: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.knob02s, mBitmapOptions)
    private var mKnobDisableBitmap: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.knob_disable, mBitmapOptions)
    private var mDrawKnob = true
    private var mDrawText = true
    private var mTouchX = 0f
    private var mTouchY = 0f
    private var mCenterX = 0f
    private var mCenterY = 0f
    private var mSmallestDim = 1
    private var mWidthBitmap = 0f
    private var mHeightBitmap = 0f
    private var mKnobDegree = 0f
    private var mKnobDial = 0f
    private var mKnobText = ""
    private var mKnobTextWidth = 0
    private var mSaturated = false
    private var tmpRect = Rect()
    private var tmpKnob = 0f
    private var mMin = 0
    private var mMax = 270
    private var mStart = 0
    private var mTextLabel = "label"
    private var mTextLabelWidth = 0f
    private var mTextLabelHeight = 0f
    private var mOutlineRect = Rect()
    private var mRotationAnchorX = 0f
    private var mRotationAnchorY = 0f
    private val mBitmapScale = 0.8f
    private var mOnChangedListener: ChangedListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DialKnob)
        val min = typedArray.getInt(R.styleable.DialKnob_minValue, 0)
        val max = typedArray.getInt(R.styleable.DialKnob_maxValue, 270)
        val start = typedArray.getInt(R.styleable.DialKnob_startValue, 0)
        val text = if ((typedArray.getText(R.styleable.DialKnob_text) != null)) {
            typedArray.getText(R.styleable.DialKnob_text).toString()
        } else {
            ""
        }
        typedArray.recycle()
        mBitmapOptions.inScaled = false
        init(min, max, start, text)
    }

    public fun interface ChangedListener {
        fun onChange(it: Int, forceSend: Boolean)
    }


    fun setOnChangedListener(onChangedListener: ChangedListener) {
        mOnChangedListener = onChangedListener
    }

    private fun init(min: Int, max: Int, start: Int, text: String) {
        mMin = min
        mMax = max
        mStart = start
        mTextLabel = text
        with(mPaintRoller) {
            isDither = true
            color = Color.BLACK
            isAntiAlias = true
            strokeWidth = 3f
            style = Paint.Style.FILL
        }

        mPaintKnob.style = Paint.Style.FILL
        mPaintKnob.isDither = true
        mPaintKnob.color = -0xffba34 // alpha.r.g.b
        mPaintKnob.style = Paint.Style.STROKE
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
            BitmapFactory.decodeResource(
                resources,
                R.drawable.knob02s,
                mBitmapOptions
            )
        mWidthBitmap = mKnobBitmap.width.toFloat()
        mHeightBitmap = mKnobBitmap.height.toFloat()
        mCenterX = width / 2f
        mCenterY = height / 2f
        Log.d("KNOB", "mMin:$mMin, mMax:$mMax, mStart:$mStart")
        mKnobDial = mStart.toFloat()
        mKnobDegree = dial2knob(dial2degree(mStart))
        mKnobText = mStart.toString()
    }

    fun setStyle(min: Int, max: Int, start: Int, text: String) {
        mMin = min
        mMax = max
        mStart = start
        mTextLabel = text
        mKnobDial = mStart.toFloat()
        mKnobDegree = dial2knob(dial2degree(mStart))
        mKnobText = mStart.toString()
        invalidate()
    }

    private fun dial2degree(dial: Int): Float {
        return Helpers.remap(dial.toFloat(), mMin.toFloat(), mMax.toFloat(), 0f, 270f)
    }

    private fun degree2dial(degree: Float): Int {
        return Helpers.remap(degree, 0f, 270f, mMin.toFloat(), mMax.toFloat()).toInt()
    }

    private fun knob2dial(knob: Float): Float {
        return 270f - Helpers.wrapAng360(knob + 45f)
    }

    private fun dial2knob(dial: Float): Float {
        return Helpers.wrapAng180(270f - 45f - dial)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSmallestDim = min(width, height)
        mPaintText.textSize = mSmallestDim * 0.1f
        mKnobBitmap = mKnobBitmap.scale(
            (mSmallestDim * mBitmapScale).toInt(),
            (mSmallestDim * mBitmapScale).toInt(),
            true
        )
        mKnobDisableBitmap = mKnobDisableBitmap.scale(
            (mSmallestDim * mBitmapScale).toInt(),
            (mSmallestDim * mBitmapScale).toInt(),
            true
        )
        mWidthBitmap = mKnobBitmap.width.toFloat() / 2
        mHeightBitmap = mKnobBitmap.height.toFloat() / 2
        mRotationAnchorX = mSmallestDim * (1f - mBitmapScale) / 2f + mWidthBitmap
        mRotationAnchorY = mSmallestDim * (1f - mBitmapScale) / 8f + mHeightBitmap
        mCenterX = width / 2f
        mCenterY = height / 2f
        mPaintText.getTextBounds(mKnobText, 0, mKnobText.length, tmpRect)
        mKnobTextWidth = tmpRect.width()
        val tmpRectLabel = Rect()
        mPaintText.getTextBounds(mTextLabel, 0, mTextLabel.length, tmpRectLabel)
        mTextLabelHeight = tmpRectLabel.height().toFloat()
        mTextLabelWidth = tmpRectLabel.width().toFloat()
        mOutlineRect.top = 1
        mOutlineRect.left = 1
        mOutlineRect.right = mSmallestDim - 1
        mOutlineRect.bottom = mSmallestDim - 1
    }

    override fun onDraw(canvas: Canvas) {
        repeat(19) {
            val x1 =
                mRotationAnchorX + cos((it.toDouble() * 15f - 45f) * Math.PI / 180f) * mSmallestDim * 0.3f
            val y1 =
                mRotationAnchorY - sin((it.toDouble() * 15f - 45f) * Math.PI / 180f) * mSmallestDim * 0.3f
            val x2 =
                mRotationAnchorX + cos((it.toDouble() * 15f - 45f) * Math.PI / 180f) * mSmallestDim * 0.40f
            val y2 =
                mRotationAnchorY - sin((it.toDouble() * 15f - 45f) * Math.PI / 180f) * mSmallestDim * 0.40f
            canvas.drawLine(
                x1.toFloat(),
                y1.toFloat(),
                x2.toFloat(),
                y2.toFloat(),
                mPaintRoller
            )
        }

        if (mDrawKnob) {
            canvas.rotate(
                -1f * (mKnobDegree - 90f),
                mRotationAnchorX,
                mRotationAnchorY
            )
            canvas.drawBitmap(
                (if (isEnabled) mKnobBitmap else mKnobDisableBitmap),
                mSmallestDim * (1 - mBitmapScale) / 2,
                mSmallestDim * (1 - mBitmapScale) / 8,
                mPaintKnob
            )
            canvas.rotate((mKnobDegree - 90f), mRotationAnchorX, mRotationAnchorY)
        }
        if (mDrawText) {
            canvas.drawText(
                mKnobText,
                mSmallestDim / 2f - (mKnobTextWidth / 2f),
                mSmallestDim * 0.7f,
                mPaintText
            )
            canvas.drawText("Min", mSmallestDim * 0.02f, mSmallestDim * 0.8f, mPaintText)
            canvas.drawText("Max", mSmallestDim * 0.78f, mSmallestDim * 0.8f, mPaintText)
            canvas.drawText(
                mTextLabel,
                mSmallestDim / 2f - mTextLabelWidth / 2,
                mSmallestDim - mTextLabelHeight,
                mPaintText
            )
        }
        //canvas.drawRect(mOutlineRect, mPaintKnob)
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (!isEnabled)
            return true
        if (event.action == MotionEvent.ACTION_UP) {
            performClick()
            if (mOnChangedListener != null) {
                mOnChangedListener?.onChange(mKnobDial.toInt(), true)
            }
            mSaturated = false
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
            if (!mSaturated && tmpKnob < -45f && tmpKnob > -135f) {
                mSaturated = true
                if (tmpKnob < -45 && tmpKnob > -90) mKnobDegree = -45f
                if (tmpKnob > -135f && tmpKnob < -90) mKnobDegree = -135f
            }
            if (!mSaturated) {
                mKnobDegree = tmpKnob
                mKnobTextWidth = tmpRect.width()
            }
            if (mSaturated && (abs(tmpKnob - mKnobDegree) < 10)) {
                mSaturated = false
            }
            mKnobDial = degree2dial(knob2dial(mKnobDegree)).toFloat()
            mKnobText = mKnobDial.toString()
            if (mOnChangedListener != null) {
                mOnChangedListener?.onChange(mKnobDial.toInt(), false)
            }
            mPaintText.getTextBounds(mKnobText, 0, mKnobText.length, tmpRect)
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
