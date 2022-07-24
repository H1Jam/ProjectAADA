package com.hjam.aada.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.hjam.aada.R
import android.view.View
import androidx.core.graphics.scale
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ArHrz : View {
    /// public List<Float> dat01 = new ArrayList<>();
    private var currentPaint: Paint? = null
    private var wallpath = Path()
    private var xc = 0f
    private var yc = 0f
    private var roll = 0f
    private var pitch = 0f
    private var canvasWidth = 0f
    private var canvasHeight = 0f
    var minDim = 0f
    private var face: Bitmap? = null
    private var sky: Bitmap? = null
    private var skyAspect = 1f
    private var mScX = 0f
    private var mAHBackWidth = 0f
    private var mAHSkyWidth = 0f
    private var mAHBackHeight = 0f
    private var mAHSkyHeight = 0f

    private var mMatrix: Matrix? = null

    fun setValues(roll: Float, pitch: Float) {
        this.roll = roll
        this.pitch = pitch
        invalidate()
        requestLayout()
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        minDim = min(w, h).toFloat()
        face = BitmapFactory.decodeResource(this.resources, R.drawable.ah_glass).scale(minDim.toInt(),minDim.toInt(),true)
        sky = BitmapFactory.decodeResource(this.resources, R.drawable.horizon_sky)
        canvasWidth = w.toFloat()
        canvasHeight = h.toFloat()
        xc = canvasWidth / 2f
        yc = canvasHeight / 2f
        mMatrix = Matrix()
        mScX = minDim / face!!.width.toFloat()
        skyAspect = (sky!!.height.toFloat() / sky!!.width)
        sky=sky!!.scale(
            (minDim*0.75f).toInt(),
            (minDim*0.75f*skyAspect).toInt()
        )
        mAHBackWidth = face!!.width.toFloat()
        mAHSkyWidth = sky!!.width.toFloat()
        mAHBackHeight = face!!.height.toFloat()
        mAHSkyHeight = sky!!.height.toFloat()
        wallpath.reset()
        wallpath.moveTo(minDim*0.95f, minDim/2);
        repeat(60) {
            wallpath.lineTo(minDim/2 + (minDim * 0.45f * cos(it * Math.PI / 30f)).toFloat(), minDim/2 + (minDim * 0.45f * sin(it * Math.PI / 30)).toFloat())
        }
    }

    private fun init() {
        currentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        currentPaint?.isDither = false
        currentPaint?.color = Color.argb(255, 234, 28, 58) // alpha.r.g.b
        currentPaint?.style = Paint.Style.STROKE
        currentPaint?.strokeJoin = Paint.Join.ROUND
        currentPaint?.strokeCap = Paint.Cap.ROUND
        currentPaint?.strokeWidth = 5f
        currentPaint?.isAntiAlias = true
        currentPaint?.setShadowLayer(10f, 3f, 3f, Color.BLACK)
        mMatrix = Matrix()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        // matrix.setScale(MinDim/face.getWidth(),MinDim/face.getWidth(),0, 0);
        //  matrix.postTranslate((canvasWidth-MinDim)/2,(canvasHeight-MinDim)/2);

        //  canvas.drawBitmap(face, matrix,currentPaint);


        //  matrix.setRotate(10, sky.getWidth()/2, sky.getHeight()/2);

        //  matrix.setScale(MinDim/sky.getWidth(),MinDim/sky.getWidth(),0, 0);
        //  matrix.postTranslate((canvasWidth-MinDim)/2,(canvasHeight-MinDim)/2);



//        wallpath.moveTo(canvasWidth*0.8f,yc);
//        wallpath.lineTo(canvasWidth*0.2f,yc+100);
        currentPaint!!.alpha = 255
        currentPaint!!.strokeWidth = 2f
        currentPaint!!.style = Paint.Style.STROKE
        //matrix = new Matrix();
        //pitch = 30f
        //roll = 45f
        mScX = minDim / sky!!.width.toFloat()
        mMatrix?.reset()
        mMatrix?.preRotate(roll, mAHBackWidth / 2.0f, mAHBackHeight / 2.0f)
        mMatrix?.preTranslate(
            minDim/2 - mAHSkyWidth / 2.0f,
            minDim/2 - mAHSkyHeight * (0.5f - 0.36f * pitch / 90.0f)
        )
        canvas.save()
        canvas.clipPath(wallpath)
        canvas.drawBitmap(sky!!, mMatrix!!, currentPaint)
        canvas.restore()
        currentPaint!!.strokeWidth = minDim / 200f
        currentPaint!!.color = Color.WHITE
        currentPaint!!.style = Paint.Style.FILL
        currentPaint!!.alpha = 255

        canvas.drawLine(minDim * 0.2f, minDim/2, minDim * 0.8f, minDim/2, currentPaint!!)

        canvas?.drawBitmap(
            face!!,
            0f,
            0f,
            currentPaint)
        //
        //canvas.drawLine(minDim * 0.2f, minDim/2, minDim * 0.8f, minDim/2, currentPaint!!)
        // canvas.drawColor(Color.TRANSPARENT);
        // canvas.drawRect(1,1,canvasWidth-1,canvasHeight-1,PaintSTRK);
        //   canvas.drawBitmap(AHR,0,0,currentPaint);
    }
}