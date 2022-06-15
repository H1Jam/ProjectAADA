package com.hjam.aada.comm.types

class AnalogGauge(
    private val x: Int,
    private val y: Int,
    private val width: Int,
    private val height: Int,
    private var value: Float
) {
    companion object{
        const val mID : Byte = 16.toByte()
        val typeArrayList = arrayOf(Long, Float, Int)
    }
    public fun setX() {

    }

}