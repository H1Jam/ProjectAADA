package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADAKnob(
    var x: Int,
    var y: Int,
    val tag: Int,
    val cmdId: Int,
    var size: Int,
    val minValue: Int,
    val maxValue: Int,
    val startValue: Int,
    val labelText: String
) : AADAObject(tag, "knb") {
    var dial: Int = startValue
    var lastCallbackTick: Long = 0L

    companion object {
        val objID = ScreenIDs.Knob.ordinal.toByte()
        const val minCallTime = 50
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADAKnob {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val cmdId = byteBuffer.get().toInt()
            val size = byteBuffer.short.toInt()
            val minValue = byteBuffer.short.toInt()
            val maxValue = byteBuffer.short.toInt()
            val startValue = byteBuffer.short.toInt()
            val vText = StandardCharsets.UTF_8.decode(byteBuffer).toString()
            return AADAKnob(x, y, cTag, cmdId, size, minValue, maxValue, startValue, vText)
        }

        fun toBytesFromTag(aadaKnob: AADAKnob): ByteArray? {
            val bb1 = ByteBuffer.allocate(Byte.SIZE_BYTES + Short.SIZE_BYTES + Short.SIZE_BYTES)
            bb1.order(ByteOrder.LITTLE_ENDIAN)
            val array = with(bb1) {
                put(objID)
                putShort(aadaKnob.tag.toShort())
                putShort(aadaKnob.dial.toShort())
            }.array()
            return array
        }
    }

    override fun toString(): String {
        return "AADAKnob:[Tag:$tag (${screenTag}),cmdId:$cmdId x:$x, y:$y, Size:$size " +
                "minValue:$minValue, maxValue:$maxValue," +
                " startValue:$startValue, labelText:$labelText]"
    }
}