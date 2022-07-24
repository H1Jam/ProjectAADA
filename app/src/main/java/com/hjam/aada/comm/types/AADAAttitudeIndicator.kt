package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder

class AADAAttitudeIndicator(
    var x: Int,
    var y: Int,
    val tag: Int,
    val cmdId: Int,
    val size: Int,
    var roll: Float,
    var pitch: Float
) : AADAObject(tag, "att") {
    companion object {
        val objID = ScreenIDs.AttitudeIndicator.ordinal.toByte()
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADAAttitudeIndicator {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val size = byteBuffer.short.toInt()
            val cmdId = byteBuffer.get().toInt()
            val roll = byteBuffer.float
            val pitch = byteBuffer.float
            return AADAAttitudeIndicator(
                x,
                y,
                cTag,
                cmdId,
                size,
                roll,
                pitch
            )
        }
    }

    override fun toString(): String {
        return "AADAGauge:[Tag:$tag (${screenTag}), x:$x, y:$y, cmdId:$cmdId Size:$size, " +
                "roll:$roll, pitch:$pitch]"
    }
}
