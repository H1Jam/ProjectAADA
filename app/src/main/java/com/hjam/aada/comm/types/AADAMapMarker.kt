package com.hjam.aada.comm.types

import com.hjam.aada.R
import java.nio.ByteBuffer
import java.nio.ByteOrder

class AADAMapMarker(
    var lat: Float,
    var lon: Float,
    var rotation: Float,
    val iconId: Int,
    val cmdId: Int,
    val tag: Int
) : AADAObject(tag, "mrk") {
    companion object {
        val objID = ScreenIDs.MapMarkers.ordinal.toByte()

        // Will extend it.
        enum class Icons(anchorX: Float, anchorY: Float, isFlat: Boolean, drawableId: Int) {
            Default(0.5f, 0f, false, 0),
            Car_Red(0.5f, 0.5f, true, R.drawable.car_red),
            Car_Blue(0.5f, 0.5f, true, R.drawable.car_blue),
            Car_Green(0.5f, 0.5f, true, R.drawable.car_green),
            Car_Yellow(0.5f, 0.5f, true, R.drawable.car_yellow),
            Plane_Red(0.5f, 0.5f, true, R.drawable.plane_red),
            Plane_Blue(0.5f, 0.5f, true, R.drawable.plane_blue),
            Plane_Green(0.5f, 0.5f, true, R.drawable.plane_green)
        }

        enum class MarkerCmdId {
            Add,
            Modify,
            Remove
        }

        fun fromByteBuffer(byteBuffer: ByteBuffer): AADAMapMarker {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val cTag = byteBuffer.short.toInt()
            val lat = byteBuffer.float
            val lon = byteBuffer.float
            val rotation = byteBuffer.float
            val iconId = byteBuffer.short.toInt()
            val cmdId = byteBuffer.get().toInt() // MarkerCmdId's ordinal
            return AADAMapMarker(
                lat,
                lon,
                rotation,
                iconId,
                cmdId,
                cTag
            )
        }
    }

    override fun toString(): String {
        return "AADAMapMarker:[Tag:$tag (${screenTag}), lat:$lat, lon:$lon, rotation:$rotation, " +
                " iconId:$iconId, cmdId:$cmdId]"
    }
}
