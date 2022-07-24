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
    private var paintC = Paint(Paint.ANTI_ALIAS_FLAG)
    var wallpath = Path()
    private var xe = 0f
    private var ye = 0f
    var radius = 100f
    var xc = 0f
    var yc = 0f
    private var roll = 0f
    private var pitch = 0f
    var canvasWidth = 0f
    var canvasHeight = 0f
    var minDim = 0f
    var AHR: Bitmap? = null
    var canvasAH: Canvas? = null

    // float[] xP= new float[4];
    // float[] yP=new float[4];
    private var face: Bitmap? = null
    private var sky: Bitmap? = null
    private var skyAspect = 1f
    private var df: DecimalFormat? = null
    private var Arect: RectF? = null
    private var scX = 0f
    private var scY = 0f
    private var AHBackWidth = 0f
    private var AHSkyWidth = 0f
    private var AHBackHeight = 0f
    private var AHSkyHeight = 0f

    // RectF gPointer=new RectF(10,10,10,10);
    //Bitmap icon1;
    private var mMatrix: Matrix? = null

    fun setValus(_roll: Float, _pitch: Float) {
        roll = _roll
        pitch = _pitch
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
        face = BitmapFactory.decodeResource(this.resources, R.drawable.ah_background2).scale(minDim.toInt(),minDim.toInt(),true)
        sky = BitmapFactory.decodeResource(this.resources, R.drawable.horizon_sky)
        canvasWidth = w.toFloat()
        canvasHeight = h.toFloat()
        xc = canvasWidth / 2f
        yc = canvasHeight / 2f
        mMatrix = Matrix()
        scX = minDim / face!!.width.toFloat()
        scY = (scX*w)
        skyAspect = (sky!!.height.toFloat() / sky!!.width)
        sky=sky!!.scale(
            (minDim*0.75f).toInt(),
            (minDim*0.75f*skyAspect).toInt()
        )
        AHBackWidth = face!!.width.toFloat()
        AHSkyWidth = sky!!.width.toFloat()
        AHBackHeight = face!!.height.toFloat()
        AHSkyHeight = sky!!.height.toFloat()
        //paintC.strokeWidth = minDim * 0.2f
       // paintC.color = Color.TRANSPARENT
        //paintC.xfermode = PorterDuffXfermode(PorterDuff.Mode.ADD)
        wallpath.reset()
        wallpath.moveTo(minDim*0.95f, minDim/2);
        repeat(60) {
            wallpath.lineTo(minDim/2 + (minDim * 0.45f * cos(it * Math.PI / 30f)).toFloat(), minDim/2 + (minDim * 0.45f * sin(it * Math.PI / 30)).toFloat())
        }
    }

    private fun init() {

        df = DecimalFormat("#")
        //        icon1 = BitmapFactory.decodeResource(this.getResources(),
//                R.drawable.electro);
//        icon1=Bitmap.createScaledBitmap(icon1, 100, 100, true);
        currentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        currentPaint?.isDither = false
        currentPaint?.color = Color.argb(255, 234, 28, 58) // alpha.r.g.b
        currentPaint?.style = Paint.Style.STROKE
        currentPaint?.strokeJoin = Paint.Join.ROUND
        currentPaint?.strokeCap = Paint.Cap.ROUND
        currentPaint?.strokeWidth = 5f
        currentPaint?.isAntiAlias = true
        currentPaint?.setShadowLayer(10f, 3f, 3f, Color.BLACK)
//        paintC = Paint()
//        paintC.color = Color.TRANSPARENT
//        paintC.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        paintC.strokeWidth = minDim * 0.2f
//        paintC.style = Paint.Style.STROKE
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
        scX = minDim / sky!!.width.toFloat()
        mMatrix?.reset()
        mMatrix?.preRotate(roll, AHBackWidth / 2.0f, AHBackHeight / 2.0f)
        mMatrix?.preTranslate(
            minDim/2 - AHSkyWidth / 2.0f,
            minDim/2 - AHSkyHeight * (0.5f - 0.36f * pitch / 90.0f)
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