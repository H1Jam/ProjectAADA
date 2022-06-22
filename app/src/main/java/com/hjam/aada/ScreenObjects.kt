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
import com.hjam.aada.comm.types.TextLabel
import com.hjam.aada.utils.Logger


object ScreenObjects {
    private val mTag = "ScreenObjects"
    private val mTexLabelsList: MutableList<TextLabel> = mutableListOf()
    private var mTopToTop = 0
    private var mLeftToLeft = 0
    private lateinit var mCanvasConstraintLayout: ConstraintLayout
    private lateinit var mDisplayMetrics: DisplayMetrics
    private var mReady = false

    enum class ViewColors {
        Red,
        Blue,
        White,
        Black,
        Yellow
    }

    private fun refreshScreen() {
        for (tl in mTexLabelsList) {
            Logger.debug(mTag, "UI: $tl")
        }
    }

    fun initScreen(
        canvasConstraintLayout: ConstraintLayout, topToTop: Int, leftToLeft: Int,
        displayMetrics: DisplayMetrics
    ) {
        mTopToTop = topToTop
        mLeftToLeft = leftToLeft
        mCanvasConstraintLayout = canvasConstraintLayout
        mDisplayMetrics = displayMetrics
        mReady = true
    }

    fun addTextLabel(textLabel: TextLabel) {
        if (!mReady || textLabel.tag <= 0) {
            return
        }
        Logger.debug(mTag, "addTextLabel: ${textLabel.tag}")
        for (tl in mTexLabelsList) {
            if (tl.tag == textLabel.tag) {
                modifyTextLabel(textLabel)
                return
            }
        }
        mTexLabelsList.add(textLabel)
        addTextToScreen(textLabel)
        refreshScreen()
    }

    private fun modifyTextLabel(textLabel: TextLabel) {
        Logger.debug(mTag, "modifyTextLabel: ${textLabel.tag}")
        for (i in 0..mTexLabelsList.size) {
            if (mTexLabelsList[i].tag == textLabel.tag) {
                mTexLabelsList[i].updateFromInstance(textLabel)
                refreshText(textLabel)
                refreshScreen()
                return
            }
        }
    }

    fun removeTextLabel(textLabel: TextLabel) {
        for (tl in mTexLabelsList) {
            if (tl.tag == textLabel.tag) {
                mTexLabelsList.remove(tl)
                return
            }
        }
    }

   fun addTextToScreen(textLabel: TextLabel){
       with(textLabel){
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

    fun refreshText(textLabel: TextLabel) {
        val lbl: TextView? = mCanvasConstraintLayout.findViewWithTag("lbl${textLabel.tag.toUInt()}")
        if (lbl != null) {
            lbl.text = textLabel.text
            if (textLabel.textColor.toUInt() > 0u){
                lbl.setTextColor(textLabel.textColor)
            }
            if (textLabel.fontSize > 0){
                lbl.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textLabel.fontSize.toFloat())
            }
        } else {
            Logger.error(mTag, "refreshText: TextView(tag=${textLabel.tag}) does not exist!")
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
        btn.tag = "btn${cTag.toUInt()}"
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
                val str = "TAG:${button.tag} $numClicks"
                button.text = str
                sendButtonClickData(v?.tag.toString())
            }
        }
        button.setOnClickListener(buttonListener)
    }

    fun sendButtonClickData(tag: String) {
        try {
            val pTag: UByte = (Integer.parseInt(tag.drop(3))).toUByte() and 0xFF.toUByte()
            Logger.debug(mTag, "sendBtnClickData: $pTag")
        } catch (ex: Exception) {
            Logger.debug(mTag, "sendBtnClickData: ${ex.message}")
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


    private fun getDrawable(color: Int): Drawable? {
        return AppCompatResources.getDrawable(
            AADATheApp.instance.applicationContext,
            R.drawable.button_red
        )?.apply { setTint(color) }
    }

    private fun getDrawableForegroundColor(color: ViewColors): Int {
        return when (color) {
            (ViewColors.Red) -> {
                Color.BLACK
            }
            (ViewColors.Blue) -> {
                Color.WHITE
            }
            (ViewColors.Black) -> {
                Color.WHITE
            }
            (ViewColors.White) -> {
                Color.BLACK
            }
            (ViewColors.Yellow) -> {
                Color.BLACK
            }
            else -> {
                Color.BLACK
            }
        }
    }

}