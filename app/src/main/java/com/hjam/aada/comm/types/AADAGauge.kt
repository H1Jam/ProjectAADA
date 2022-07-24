package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADAGauge(
    var x: Int,
    var y: Int,
    val tag: Int,
    val cmdId: Int,
    val size: Int,
    var value: Float,
    val maxValue: Float,
    val drawArc: Boolean,
    val arcGreenMaxVal: Float,
    val arcYellowMaxVal: Float,
    val arcRedMaxVal: Float,
    val unitTextLabel: String
) : AADAObject(tag, "gau") {
    companion object {
        val objID = ScreenIDs.Gauge1.ordinal.toByte()
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADAGauge {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val size = byteBuffer.short.toInt()
            val value = byteBuffer.float
            val maxValue = byteBuffer.float
            val drawArc = byteBuffer.get() != 0.toByte()
            val arcGreenMaxVal = byteBuffer.float
            val arcYellowMaxVal = byteBuffer.float
            val arcRedMaxVal = byteBuffer.float
            val unitTextLabel = StandardCharsets.UTF_8.decode(byteBuffer).toString()
            return AADAGauge(
                x,
                y,
                cTag,
                0,
                size,
                value,
                maxValue,
                drawArc,
                arcGreenMaxVal,
                arcYellowMaxVal,
                arcRedMaxVal,
                unitTextLabel,

            )
        }
    }

    override fun toString(): String {
        return "AADAGauge:[Tag:$tag (${screenTag}), x:$x, y:$y, Size:$size, value:$value, " +
                "maxValue:$maxValue, drawArc:$drawArc, arcGreenMaxVal:$arcGreenMaxVal, " +
                "arcYellowMaxVal:$arcYellowMaxVal, arcRedMaxVal:$arcRedMaxVal, " +
                "unitTextLabel:$unitTextLabel]"
    }

}