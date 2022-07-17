package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADAMap(
    val x: Int,
    val y: Int,
    val height: Int,
    val width: Int,
    val tag: Int
) : AADAObject(tag, "map")  {
    val objID = ScreenIDs.Map.ordinal.toByte()
    fun fromByteBuffer(byteBuffer: ByteBuffer): AADAMap {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
        val x = byteBuffer.short.toInt()
        val y = byteBuffer.short.toInt()
        val cTag = byteBuffer.short.toInt()
        val height = byteBuffer.short.toInt()
        val width = byteBuffer.short.toInt()
        return AADAMap(
            x,
            y,
            height,
            width,
            cTag
        )
    }

    override fun toString(): String {
        return "AADAMap:[Tag:$tag (${screenTag}), x:$x, y:$y, height:$height, width:$width]"
    }
}