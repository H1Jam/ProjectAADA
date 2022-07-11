package com.hjam.aada.widgets

import com.hjam.aada.comm.types.AADAObject

class AnalogGauge(
    private var tag: String,
    private val x: Int,
    private val y: Int,
    private val width: Int,
    private val height: Int,
    private var value: Float
) {
    companion object{
        val objID : Byte = AADAObject.ScreenIDs.gauge1.ordinal.toByte()
        val typeArrayList = arrayOf(Long, Float, Int)
    }
    public fun setX() {

    }

}