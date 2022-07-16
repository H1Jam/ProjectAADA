package com.hjam.aada

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.hjam.aada.comm.DataProtocol
import com.hjam.aada.comm.types.*
import com.hjam.aada.utils.Logger
import com.hjam.aada.widgets.DialKnob
import com.hjam.aada.widgets.GaugeView


object ScreenObjects {
    private const val mTag = "ScreenObjects"
    private val mScreenObjects: MutableSet<String> = mutableSetOf()
    private var mTopToTop = 0
    private var mLeftToLeft = 0
    private lateinit var mCanvasConstraintLayout: ConstraintLayout
    private lateinit var mDisplayMetrics: DisplayMetrics
    private var mReady = false
    private var mWriteListener: AADAWriter? = null

    enum class ViewColors {
        Red,
        Blue,
        White,
        Black,
        Yellow
    }

    private fun refreshScreen() {
        //   for (tl in mScreenObjects) {
        Logger.debug(mTag, "UI: ${mScreenObjects.joinToString()}")
        // }
    }

    fun initScreen(
        canvasConstraintLayout: ConstraintLayout, topToTop: Int, leftToLeft: Int,
        displayMetrics: DisplayMetrics, writeListener: AADAWriter
    ) {
        mTopToTop = topToTop
        mLeftToLeft = leftToLeft
        mCanvasConstraintLayout = canvasConstraintLayout
        mDisplayMetrics = displayMetrics
        mReady = true
        mWriteListener = writeListener
    }

    fun addKnob(aadaKnob: AADAKnob) {
        if (mReady && aadaKnob.tag > 0) {
            Logger.debug(mTag, "addKnob: $aadaKnob")
            if (mScreenObjects.add(aadaKnob.screenTag)) {
                addKnobToScreen(aadaKnob)
            } else {
                modifyKnob(aadaKnob)
            }
            refreshScreen()
        }
    }

    fun addGauge(aadaGauge: AADAGauge) {
        if (mReady && aadaGauge.tag > 0) {
            Logger.debug(mTag, "aadaGauge: $aadaGauge")
            if (mScreenObjects.add(aadaGauge.screenTag)) {
                addGaugeToScreen(aadaGauge)
            } else {
                modifyGauge(aadaGauge)
            }
            refreshScreen()
        }
    }

    fun addButton(aadaButton: AADAButton) {
        if (mReady && aadaButton.tag > 0) {
            Logger.debug(mTag, "addButton: ${aadaButton.tag}")
            if (mScreenObjects.add(aadaButton.screenTag)) {
                addButtonToScreen(aadaButton)
            } else {
                modifyButton(aadaButton)
            }
            refreshScreen()
        }
    }

    private fun modifyButton(aadaButton: AADAButton) {
        Logger.debug(mTag, "modifyButton: ${aadaButton.tag}")
        refreshButton(aadaButton)
        refreshScreen()
    }

    private fun modifyKnob(aadaKnob: AADAKnob) {
        Logger.debug(mTag, "modifyKnob: ${aadaKnob.tag}")
        refreshKnob(aadaKnob)
        refreshScreen()
    }

    private fun modifyGauge(aadaGauge: AADAGauge) {
        Logger.debug(mTag, "modifyKnob: ${aadaGauge.tag}")
        refreshGauge(aadaGauge)
        refreshScreen()
    }

    fun addTextLabel(aadaTextLabel: AADATextLabel) {
        if (mReady && aadaTextLabel.tag > 0) {
            Logger.debug(mTag, "addTextLabel: ${aadaTextLabel.tag}")
            if (mScreenObjects.add(aadaTextLabel.screenTag)) {
                addTextToScreen(aadaTextLabel)
                refreshScreen()
            } else {
                modifyTextLabel(aadaTextLabel)
            }

        }
    }

    private fun modifyTextLabel(aadaTextLabel: AADATextLabel) {
        Logger.debug(mTag, "modifyTextLabel: ${aadaTextLabel.tag}")
        refreshText(aadaTextLabel)
        refreshScreen()
    }

    fun removeTextLabel(AADATextLabel: AADATextLabel) {

    }

    private fun addTextToScreen(aadaTextLabel: AADATextLabel) {
        with(aadaTextLabel) {
            val params = setLayout(
                x,
                y,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            Logger.debug(
                mTag, "addTextToScreen:[x:$x, y:$y, " +
                        "cTag:$screenTag, Text:$text], size:$fontSize"
            )
            val lbl = TextView(AADATheApp.instance.applicationContext)
            lbl.tag = screenTag
            lbl.text = text
            lbl.layoutParams = params
            if (textColor.toUInt() > 0U) {
                lbl.setTextColor(textColor)
            }
            lbl.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize.toFloat())
            mCanvasConstraintLayout.addView(lbl)

        }
    }

    fun addTextToScreen(x: Int, y: Int, cTag: Int, vText: String, fSize: Int, textColor: Int) {
        addTextLabel(AADATextLabel(x, y, cTag, vText, fSize, textColor))
    }

    fun refreshText(tag: Int, text: String) {
        val lbl: TextView? =
            mCanvasConstraintLayout.findViewWithTag("${AADATextLabel.mTagPrefix}${tag.toUInt()}")
        if (lbl != null) {
            lbl.text = text
        } else {
            Logger.error(mTag, "refreshText: TextView(tag=$tag) does not exist!")
        }
    }

    private fun refreshButton(aadaButton: AADAButton) {
        val btn: Button? =
            mCanvasConstraintLayout.findViewWithTag(aadaButton.screenTag)
        if (btn != null) {
            btn.text = aadaButton.text
            if (aadaButton.textColor.toUInt() > 0u) {
                btn.setTextColor(aadaButton.textColor)
            }
            if (aadaButton.backColor.toUInt() > 0u) {
                btn.background = getDrawable(aadaButton.backColor)
            }

            if (aadaButton.fontSize > 0) {
                btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, aadaButton.fontSize.toFloat())
            }
        } else {
            Logger.error(mTag, "refreshText: TextView(tag=${aadaButton.tag}) does not exist!")
        }
    }

    private fun refreshKnob(aadaKnob: AADAKnob) {
        val knb: DialKnob? =
            mCanvasConstraintLayout.findViewWithTag(aadaKnob.screenTag)
        if (knb != null) {
            with(aadaKnob) {
                knb.setStyle(minValue, maxValue, startValue, labelText)
            }
        } else {
            Logger.error(mTag, "refresh Knob: AADAKnob(tag=${aadaKnob.tag}) does not exist!")
        }
    }

    private fun refreshGauge(aadaGauge: AADAGauge) {
        val gge: GaugeView? =
            mCanvasConstraintLayout.findViewWithTag(aadaGauge.screenTag)
        if (gge != null) {
            gge.setGaugeVal(aadaGauge.value)
        } else {
            Logger.error(mTag, "refresh Gauge: AADAGauge(tag=${aadaGauge.tag}) does not exist!")
        }
    }

    private fun refreshText(aadaTextLabel: AADATextLabel) {
        val lbl: TextView? =
            mCanvasConstraintLayout.findViewWithTag(aadaTextLabel.screenTag)
        if (lbl != null) {
            lbl.text = aadaTextLabel.text
            if (aadaTextLabel.textColor.toUInt() > 0u) {
                lbl.setTextColor(aadaTextLabel.textColor)
            }
            if (aadaTextLabel.fontSize > 0) {
                lbl.setTextSize(TypedValue.COMPLEX_UNIT_DIP, aadaTextLabel.fontSize.toFloat())
            }
        } else {
            Logger.error(mTag, "refreshText: TextView(tag=${aadaTextLabel.tag}) does not exist!")
        }
    }


    fun addButtonToScreen(x: Int, y: Int, cTag: Int, vText: String, fSize: Int) {
        addButton(AADAButton(x, y, cTag, vText, fSize, Color.BLACK, Color.LTGRAY))
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
        addButton(AADAButton(x, y, cTag, vText, fSize, textColor, backColor))
    }

    private fun addButtonToScreen(aadaButton: AADAButton) {
        with(aadaButton) {
            if (tag < 0 || tag > 255) {
                Logger.error(mTag, "addButtonToScreen: Invalid tag! (tag:$tag)")
                return
            }
            val params = setLayout(
                x,
                y,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            Logger.debug(
                mTag, "addButtonToScreen:[x:$x, y:$y, " +
                        "cTag:$screenTag, Text:$text], size:$fontSize"
            )
            val btn = Button(AADATheApp.instance.applicationContext)
            btn.tag = screenTag
            btn.text = text
            btn.layoutParams = params
            btn.isAllCaps = false
            btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize.toFloat())
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

    private fun addKnobToScreen(aadaKnob: AADAKnob) {
        with(aadaKnob) {
            if (tag < 0 || tag > 255) {
                Logger.error(mTag, "addKnobToScreen: Invalid tag! (tag:$tag)")
                return
            }
            val params = setLayout(x, y, size, size)
            val knb = DialKnob(AADATheApp.instance.applicationContext)
            knb.tag = screenTag
            knb.layoutParams = params
            val pad = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15.0F,
                mDisplayMetrics
            ).toInt()
            knb.setPaddingRelative(pad, 0, pad, 0)
            knb.setStyle(minValue, maxValue, startValue, labelText)
            mCanvasConstraintLayout.addView(knb)

            knb.setOnChangedListener { it, forced ->
                if (!forced && (System.currentTimeMillis() - aadaKnob.lastCallbackTick) > AADAKnob.minCallTime) {
                    Logger.debug("Knob", "Knob send $aadaKnob it:$it,")
                    lastCallbackTick = System.currentTimeMillis()
                    writeData(AADAKnob.toBytesFromTag(aadaKnob)
                        ?.let { it2 -> DataProtocol.prepareFrame(it2) })
                    lastCallbackTick = System.currentTimeMillis()
                }
                if (forced) {
                    Logger.debug("Knob", "Knob send(Forced) $aadaKnob it:$it,")
                    writeData(AADAKnob.toBytesFromTag(aadaKnob)
                        ?.let { it2 -> DataProtocol.prepareFrame(it2) })
                    lastCallbackTick = System.currentTimeMillis()
                }
            }
        }
    }

    private fun addGaugeToScreen(aadaGauge: AADAGauge) {
        with(aadaGauge) {
            if (tag < 0 || tag > 255) {
                Logger.error(mTag, "addGaugeToScreen: Invalid tag! (tag:$tag)")
                return
            }
            val params = setLayout(x, y, size, size)
            val gge = GaugeView(AADATheApp.instance.applicationContext)
            gge.tag = screenTag
            gge.layoutParams = params
            val pad = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                15.0F,
                mDisplayMetrics
            ).toInt()
            gge.setPaddingRelative(pad, 0, pad, 0)
            gge.setStyle(
                maxValue,
                drawArc,
                arcGreenMaxVal,
                arcYellowMaxVal,
                arcRedMaxVal,
                unitTextLabel
            )
            gge.setGaugeVal(value)
            mCanvasConstraintLayout.addView(gge)
        }
    }

    private fun setLayout(x: Int, y: Int, width: Int, height: Int): ConstraintLayout.LayoutParams {
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

        val pw = if (width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                width.toFloat(),
                mDisplayMetrics
            ).toInt()
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        val ph = if (height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                height.toFloat(),
                mDisplayMetrics
            ).toInt()
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }


        val params = ConstraintLayout.LayoutParams(ph, pw)

        with(params) {
            topToTop = mTopToTop
            leftToLeft = mLeftToLeft
            setMargins(px, py, 0, 0)
        }
        return params
    }

    private fun writeData(buffer: ByteArray?) {
        if (mWriteListener != null) {
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