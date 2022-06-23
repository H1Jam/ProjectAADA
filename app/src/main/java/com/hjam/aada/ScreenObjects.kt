package com.hjam.aada

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.hjam.aada.comm.DataProtocol
import com.hjam.aada.comm.types.AADAButton
import com.hjam.aada.comm.types.AADAWriter
import com.hjam.aada.comm.types.AADATextLabel
import com.hjam.aada.utils.Logger


object ScreenObjects {
    private val mTag = "ScreenObjects"
    private val M_TEX_LABELS_LIST: MutableList<AADATextLabel> = mutableListOf()
    private var mTopToTop = 0
    private var mLeftToLeft = 0
    private lateinit var mCanvasConstraintLayout: ConstraintLayout
    private lateinit var mDisplayMetrics: DisplayMetrics
    private var mReady = false
    private var mWriteListener : AADAWriter? =null

    enum class ViewColors {
        Red,
        Blue,
        White,
        Black,
        Yellow
    }

    private fun refreshScreen() {
        for (tl in M_TEX_LABELS_LIST) {
            Logger.debug(mTag, "UI: $tl")
        }
    }

    fun initScreen(
        canvasConstraintLayout: ConstraintLayout, topToTop: Int, leftToLeft: Int,
        displayMetrics: DisplayMetrics, writeListener:AADAWriter
    ) {
        mTopToTop = topToTop
        mLeftToLeft = leftToLeft
        mCanvasConstraintLayout = canvasConstraintLayout
        mDisplayMetrics = displayMetrics
        mReady = true
        mWriteListener = writeListener
    }

    fun addTextLabel(AADATextLabel: AADATextLabel) {
        if (!mReady || AADATextLabel.tag <= 0) {
            return
        }
        Logger.debug(mTag, "addTextLabel: ${AADATextLabel.tag}")
        for (tl in M_TEX_LABELS_LIST) {
            if (tl.tag == AADATextLabel.tag) {
                modifyTextLabel(AADATextLabel)
                return
            }
        }
        M_TEX_LABELS_LIST.add(AADATextLabel)
        addTextToScreen(AADATextLabel)
        refreshScreen()
    }

    private fun modifyTextLabel(AADATextLabel: AADATextLabel) {
        Logger.debug(mTag, "modifyTextLabel: ${AADATextLabel.tag}")
        for (i in 0..M_TEX_LABELS_LIST.size) {
            if (M_TEX_LABELS_LIST[i].tag == AADATextLabel.tag) {
                M_TEX_LABELS_LIST[i].updateFromInstance(AADATextLabel)
                refreshText(AADATextLabel)
                refreshScreen()
                return
            }
        }
    }

    fun removeTextLabel(AADATextLabel: AADATextLabel) {
        for (tl in M_TEX_LABELS_LIST) {
            if (tl.tag == AADATextLabel.tag) {
                M_TEX_LABELS_LIST.remove(tl)
                return
            }
        }
    }

   fun addTextToScreen(AADATextLabel: AADATextLabel){
       with(AADATextLabel){
           addTextToScreen(x, y,tag,text,fontSize,textColor)
       }
    }

    fun addTextToScreen(x: Int, y: Int, cTag: Int, vText: String, fSize: Int, textColor: Int?) {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            x.toFloat(),
            mDisplayMetrics
        ).toInt()
        val py = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            y.toFloat(),
            mDisplayMetrics
        ).toInt()
        val params = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.topToTop = mTopToTop
        params.leftToLeft = mLeftToLeft
        params.setMargins(px, py, 0, 0)
        Logger.debug(
            mTag, "addTextToScreen:[x:$x($px), y:$y($py), " +
                    "cTag:${cTag.toUInt()}, Text:$vText], size:$fSize"
        )
        val lbl = TextView(AADATheApp.instance.applicationContext)
        lbl.tag = "lbl${cTag.toUInt()}"
        lbl.text = vText
        lbl.layoutParams = params
        if (textColor != null) {
            lbl.setTextColor(textColor)
        }
        lbl.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fSize.toFloat())
        // SetBtListener(btn[i]);
        mCanvasConstraintLayout.addView(lbl)
        //mCanvasConstraintLayout.requestLayout()
        // SetBtListener(btn)
    }

    fun refreshText(tag: Int, text : String ){
        val lbl: TextView? = mCanvasConstraintLayout.findViewWithTag("lbl${tag.toUInt()}")
        if (lbl != null) {
            lbl.text =text
        } else {
            Logger.error(mTag, "refreshText: TextView(tag=$tag) does not exist!")
        }
    }

    fun refreshText(AADATextLabel: AADATextLabel) {
        val lbl: TextView? = mCanvasConstraintLayout.findViewWithTag("lbl${AADATextLabel.tag.toUInt()}")
        if (lbl != null) {
            lbl.text = AADATextLabel.text
            if (AADATextLabel.textColor.toUInt() > 0u){
                lbl.setTextColor(AADATextLabel.textColor)
            }
            if (AADATextLabel.fontSize > 0){
                lbl.setTextSize(TypedValue.COMPLEX_UNIT_DIP, AADATextLabel.fontSize.toFloat())
            }
        } else {
            Logger.error(mTag, "refreshText: TextView(tag=${AADATextLabel.tag}) does not exist!")
        }
    }

    fun addButtonToScreen(x: Int, y: Int, cTag: Int, vText: String, fSize: Int) {
        addButtonToScreen(x, y, cTag, vText, fSize, Color.BLACK, Color.LTGRAY)
    }

    fun addButtonToScreen(
        x: Int,
        y: Int,
        cTag: Int,
        vText: String,
        fSize: Int,
        textColor: Int,
        backColor: Int
    ) {
        if (cTag < 0 || cTag > 255) {
            Logger.error(mTag, "addButtonToScreen: Invalid tag! (tag:$cTag)")
            return
        }
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            x.toFloat(),
            mDisplayMetrics
        ).toInt()
        val py = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            y.toFloat(),
            mDisplayMetrics
        ).toInt()
        val params = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.topToTop = mTopToTop
        params.leftToLeft = mLeftToLeft
        params.setMargins(px, py, 0, 0)
        Logger.debug(
            mTag, "addButtonToScreen:[x:$x($px), y:$y($py), " +
                    "cTag:${cTag.toUInt()}, Text:$vText], size:$fSize"
        )

        val btn = Button(AADATheApp.instance.applicationContext)
        btn.tag = "${AADAButton.mTagPrefix}${cTag.toUInt()}"
        btn.text = vText
        btn.layoutParams = params
        btn.isAllCaps = false
        btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fSize.toFloat())
        btn.setTextColor(textColor)
        btn.background = getDrawable(backColor)
        val pad = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            15.0F,
            mDisplayMetrics
        ).toInt()

        btn.setPaddingRelative(pad, 0, pad, 0)
        mCanvasConstraintLayout.addView(btn)
        setButtonListener(btn)
    }

    private fun setButtonListener(button: Button) {
        val buttonListener: View.OnClickListener = object : View.OnClickListener {
            var numClicks = 0
            override fun onClick(v: View?) {
                numClicks++
                val str = "TAG:${v?.tag} $numClicks"
                button.text = str
                sendButtonClickData(v?.tag.toString())
            }
        }
        button.setOnClickListener(buttonListener)
    }

    fun sendButtonClickData(tag: String) {
        try {
            writeData(AADAButton.toBytesFromTag(tag)?.let { DataProtocol.prepareFrame(it) })
            Logger.debug(mTag, "sendBtnClickData: $tag")
        } catch (ex: Exception) {
            Logger.error(mTag, "sendBtnClickData:$tag, ex:${ex.message}")
        }
    }

    fun refreshButtonText(cTag: Int, vText: String) {
        val btn: Button? = mCanvasConstraintLayout.findViewWithTag("btn${cTag.toUInt()}")

        if (btn != null) {
            btn.text = vText
        } else {
            Logger.error(mTag, "refreshButtonText: Button(tag=$cTag) does not exist!")
        }
    }

    private fun writeData(buffer: ByteArray?){
        if (mWriteListener != null){
            mWriteListener?.write(buffer)
        }
    }


    private fun getDrawable(color: Int): Drawable? {
        return AppCompatResources.getDrawable(
            AADATheApp.instance.applicationContext,
            R.drawable.button_red
        )?.apply { setTint(color) }
    }


}