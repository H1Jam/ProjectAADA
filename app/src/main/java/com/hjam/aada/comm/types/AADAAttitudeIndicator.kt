package com.hjam.aada.comm.types

class AADAAttitudeIndicator(
    var x: Int,
    var y: Int,
    val tag: Int,
    val cmdId: Int,
    val size: Int,
    var roll: Float,
    var pitch: Float
): AADAObject(tag, "att") {
    companion object {
        val objID = ScreenIDs.AttitudeIndicator.ordinal.toByte()
    }

}
