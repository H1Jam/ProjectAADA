package com.hjam.aada.comm.types

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.hjam.aada.utils.Logger
import kotlin.math.atan2


class AADAKnob : AppCompatImageView {
    private val mTag = "AADAKnob"
    private val currentPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val options: BitmapFactory.Options = BitmapFactory.Options()
    private var knobBack: Bitmap = BitmapFactory.decodeResource(resources, com.hjam.aada.R.drawable.knob02s,options)
    // public Context myContext;
    private var drawKnob = false
    var drawText = false
    var left = 0f
    var top = 0f
    var right = 0f
    var bottom = 0f
    var radius = 100f
    var xt = 0f
    var yt = 0f
    var xc = 0f
    var yc = 0f
    var xLast = 0f
    var yLast = 0f
    var xDelta = 0f
    var yDelta = 0f
    var Knob = 0f
    var str1 = "STR1"

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        options.inScaled = false
        init()
    }

    //    private Handler mHandler = new Handler();
    //    private static final int FINGER_STOP_THRESHOLD = 100;
    fun getDrawKnob(): Boolean {
        return drawKnob
    }

    fun setDrawKnob(doDrawKnob: Boolean) {
        drawKnob = doDrawKnob
        invalidate()
        requestLayout()
    }


    private fun init() {
        currentPaint.isDither = true
        currentPaint.color = -0xffba34 // alpha.r.g.b
        currentPaint.style = Paint.Style.FILL_AND_STROKE
        currentPaint.strokeJoin = Paint.Join.ROUND
        currentPaint.strokeCap = Paint.Cap.ROUND
        currentPaint.strokeWidth = 5f
        currentPaint.isAntiAlias = true
        currentPaint.setShadowLayer(10f, 0f, 0f, Color.BLACK)
        paint2.color = Color.BLACK
        paint2.style = Paint.Style.FILL
        paint2.setShadowLayer(10f, 0f, 0f, Color.BLACK)
        paint2.textSize = 52f
        knobBack =BitmapFactory.decodeResource(resources, com.hjam.aada.R.drawable.knob02s,options)
        xc = width / 2f
        yc = height / 2f
    }
    private var counter =5
    override fun onDraw(canvas: Canvas) {
        if (counter<0){
            Logger.debug(mTag, "Knob: $Knob")
            counter=5
        }
        canvas.drawBitmap(knobBack,0F,0F,currentPaint)
        counter--
        xc = width / 2f
        yc = height / 2f

        //  if (drawKnob) {
        canvas.rotate(-1f * (Knob - 90f), xc, yc)
        //  }

        super.onDraw(canvas)
        radius = 0.8f * xc.coerceAtMost(yc)
        //  this.myCanvas =canvas;
        // if (drawKnob) {
        //  canvas.drawLine(xc, yc, xe, ye, currentPaint);

        //}
        if (drawText) {
            //  canvas.drawText(str1, xc * 0.19f, yc * 0.95f, paint2);
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_UP) {
            performClick()
            //  str1 ="UP";
            // invalidate();
            return true
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            // str1 ="Down";
            xLast = event.getX(0)
            yLast = event.getY(0)
             invalidate()

            //  this.draw(this.myCanvas);
            return true
        }
        if (event.action == MotionEvent.ACTION_MOVE) {
            xt = event.getX(0)
            yt = event.getY(0)
          //  xDelta = xLast - xt
          //  yDelta = yLast - yt
          //  xLast = xt
          //  yLast = yt

            // if (xDelta+yDelta <1 ){ return false;}
//            if (yt > yc) { // Down
//                Knob -= xDelta * 0.2f
//            } else {
//                Knob += xDelta * 0.2f
//            }
//            if (xt > xc) { // Right
//                Knob += yDelta * 0.2f
//            } else {
//                Knob -= yDelta * 0.2f
//            }
//            if (Knob > 240) {
//                Knob = 240f
//            }
//            if (Knob < -60) {
//                Knob = -60f
//            }
            //Todo: find the first touch point and use it as a reference point
            // and set the knob value relative to that point.
             Knob = (atan2(yc-yt,xt-xc) *180/3.1415f)


            // xe= (float) (xc + radius* Math.cos(Math.atan2(yc-yt,xt-xc)));
            // ye= (float) (yc - radius* Math.sin(Math.atan2(yc-yt,xt-xc)));


            // str1 =String.valueOf(event.getY(0));
            //  Knob=event.getX(0)/10;
             invalidate()
            //    this.draw(this.myCanvas);
            return true
        }
        //        mHandler.removeCallbacksAndMessages(null);
//        if(event.getActionMasked() != MotionEvent.ACTION_UP){
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    xDelta = 0;
//                    yDelta = 0;
//                }
//            }, FINGER_STOP_THRESHOLD);
//        }
        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        // doSomething();
        return true
    }
}
