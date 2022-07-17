package com.hjam.aada.comm.types

import android.graphics.drawable.Drawable

class AADAMapMarker(
    var lat: Float,
    var lon: Float,
    var rotation: Float,
    var isFlat : Boolean,
    var anchorX: Float,
    var anchorY: Float,
    val icon : Drawable,
    val iconId : Int,
    val tag: Int
) : AADAObject(tag, "mrk") {
    val objID = ScreenIDs.MapMarkers.ordinal.toByte()

    override fun toString(): String {
        return "AADAMapMarker:[Tag:$tag (${screenTag}), lat:$lat, lon:$lon, rotation:$rotation, " +
                "isFlat:$isFlat, anchorX:$anchorX, anchorY:$anchorY, iconId:$iconId]"
    }
}
