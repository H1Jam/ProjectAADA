package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder

class AADAMap(
    val x: Int,
    val y: Int,
    val height: Int,
    val width: Int,
    var lat : Float,
    var lon : Float,
    var zoom: Int,
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
        val lat = byteBuffer.float
        val lon = byteBuffer.float
        val zoom = byteBuffer.get().toInt()
        return AADAMap(
            x,
            y,
            height,
            width,
            lat,
            lon,
            zoom,
            cTag
        )
    }

    override fun toString(): String {
        return "AADAMap:[Tag:$tag (${screenTag}), x:$x, y:$y, height:$height, width:$width]"
    }
}