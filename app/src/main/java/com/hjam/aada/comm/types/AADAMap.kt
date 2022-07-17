package com.hjam.aada.comm.types

import org.osmdroid.views.overlay.Marker
import java.nio.ByteBuffer
import java.nio.ByteOrder

class AADAMap(
    val x: Int,
    val y: Int,
    val height: Int,
    val width: Int,
    var lat: Float,
    var lon: Float,
    var zoom: Float,
    val tag: Int = 1 // Always 1, since we only have 1 map on screen (may extend it in the future).
) : AADAObject(tag, "map") {
    companion object {
        val objID = ScreenIDs.Map.ordinal.toByte()
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADAMap {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()// Reserved
            val height = byteBuffer.short.toInt()
            val width = byteBuffer.short.toInt()
            val lat = byteBuffer.float
            val lon = byteBuffer.float
            val zoom = byteBuffer.float
            return AADAMap(
                x,
                y,
                height,
                width,
                lat,
                lon,
                zoom
            )
        }
    }
    override fun toString(): String {
        return "AADAMap:[Tag:$tag (${screenTag}), x:$x, y:$y, height:$height, width:$width, " +
                "lat:$lat, lon:$lon, zoom:$zoom]"
    }
}