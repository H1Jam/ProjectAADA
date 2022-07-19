package com.hjam.aada.comm.types

import com.hjam.aada.R
import org.osmdroid.views.overlay.Marker
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
    var mMarker :Marker? = null
    companion object {
        val objID = ScreenIDs.MapMarkers.ordinal.toByte()

        // Will extend it.
        enum class Icons(val anchorX: Float,val anchorY: Float,val isFlat: Boolean,val drawableId: Int) {
            PinRed(0.5f, 1f, false, R.drawable.map_pin_red),
            PinGreen(0.5f, 1f, false, R.drawable.map_pin_green),
            PinBlue(0.5f, 1f, false, R.drawable.map_pin_blue),
            PinYellow(0.5f, 1f, false, R.drawable.map_pin_yellow),
            CarRed(0.5f, 0.5f, true, R.drawable.car_red),
            CarGreen(0.5f, 0.5f, true, R.drawable.car_green),
            CarBlue(0.5f, 0.5f, true, R.drawable.car_blue),
            CarYellow(0.5f, 0.5f, true, R.drawable.car_yellow),
            PlaneRed(0.5f, 0.5f, true, R.drawable.plane_red),
            PlaneGreen(0.5f, 0.5f, true, R.drawable.plane_green),
            PlaneBlue(0.5f, 0.5f, true, R.drawable.plane_blue),
            PlaneYellow(0.5f, 0.5f, true, R.drawable.plane_yellow)
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
