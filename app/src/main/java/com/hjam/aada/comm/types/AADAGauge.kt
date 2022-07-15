package com.hjam.aada.comm.types

import com.hjam.aada.comm.types.AADAObject

class AADAGauge(
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