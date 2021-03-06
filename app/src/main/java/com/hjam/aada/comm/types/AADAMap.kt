package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder

class AADAMap(
    val x: Int,
    val y: Int,
    val cmdId: Int,
    val width: Int,
    val height: Int,
    var lat: Float,
    var lon: Float,
    var mapOrientation: Float,
    var zoom: Float,
    val tag: Int = 1 // Always 1, since we only have 1 map on screen (may extend it in the future).
) : AADAObject(tag, "map") {
    companion object {
        val objID = ScreenIDs.MapView.ordinal.toByte()
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADAMap {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()// Reserved
            val width = byteBuffer.short.toInt()
            val height = byteBuffer.short.toInt()
            val lat = byteBuffer.float
            val lon = byteBuffer.float
            val mapOrientation =  byteBuffer.float
            val zoom = byteBuffer.get().toFloat()
            return AADAMap(
                x,
                y,
                0,
                width,
                height,
                lat,
                lon,
                mapOrientation,
                zoom
            )
        }
    }

    override fun toString(): String {
        return "AADAMap:[Tag:$tag (${screenTag}), x:$x, y:$y, width:$width, height:$height, " +
                "lat:$lat, lon:$lon, zoom:$zoom, mapOrientation:$mapOrientation]"
    }
}