package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADAGauge(
    var x: Int,
    var y: Int,
    var size: Int,
    val maxValue: Float,
    val drawArc: Boolean,
    var arcGreenMaxVal: Float,
    var arcYellowMaxVal: Float,
    var arcRedMaxVal: Float,
    val unitTextLabel: String,
    val tag: Int
) : AADAObject(tag, "gau") {
    companion object {
        val objID = ScreenIDs.gauge1.ordinal.toByte()
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADAGauge {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val size = byteBuffer.short.toInt()
            val maxValue = byteBuffer.float
            val drawArc = byteBuffer.char != 0.toChar()
            val arcGreenMaxVal = byteBuffer.float
            val arcYellowMaxVal = byteBuffer.float
            val arcRedMaxVal = byteBuffer.float
            val unitTextLabel = StandardCharsets.UTF_8.decode(byteBuffer).toString()
            return AADAGauge(
                x,
                y,
                size,
                maxValue,
                drawArc,
                arcGreenMaxVal,
                arcYellowMaxVal,
                arcRedMaxVal,
                unitTextLabel,
                cTag
            )
        }
    }

    override fun toString(): String {
        return "AADAGauge:[Tag:$tag (${screenTag}), x:$x, y:$y, Size:$size maxValue:$maxValue," +
                " drawArc:$drawArc, arcGreenMaxVal:$arcGreenMaxVal, " +
                "arcYellowMaxVal:$arcYellowMaxVal, arcRedMaxVal:$arcRedMaxVal, " +
                "unitTextLabel:$unitTextLabel]"
    }

}