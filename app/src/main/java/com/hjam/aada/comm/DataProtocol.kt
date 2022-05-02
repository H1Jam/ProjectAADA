package com.hjam.aada.comm

import android.util.Log

object DataProtocol {

    private val dataBuf: ArrayList<Byte> = arrayListOf()
    private var dataBufOut: ArrayList<Byte> = arrayListOf()
    private val dataHeader: Array<Int> = arrayOf(199, 201, 176)
    private val dataIn: Array<Int> = arrayOf(0, 0, 0)
    private var dataStep = 0
    private var dataLength = 0

    /**
     * A byte receive callback. When a byte appears in the stream this method will be invoked.
     * The method runs on Bluetooth thread. Do not update UI here!
     * @param inp: an Int from input stream.
     * @return ArrayList<Byte> if the packed was parsed otherwise returns null.
     */
    fun parseIt(inp: Int): ArrayList<Byte>? {
        when (dataStep) {
            0 -> {
                dataIn[0] = dataIn[1]
                dataIn[1] = dataIn[2]
                dataIn[2] = inp
                if (dataIn.contentEquals(dataHeader)) {
                    Log.d("parseIt", "Got the header!")
                    dataIn.fill(0)
                    dataStep = 1
                }
            }
            1 -> {
                dataLength = inp
                dataStep = if (dataLength > 0) 2 else 0
                Log.d("parseIt", "Got the Length:[$dataLength]")
            }
            2 -> {
                dataLength--
                dataBuf.add(inp.toByte())
                if (dataLength < 1) {
                    dataStep = 0
                    Log.d("parseIt", "Got the frame:[${dataBuf.joinToString()}]")
                    if (dataBuf.size > 0) {
                        dataBufOut = dataBuf.toMutableList() as ArrayList<Byte>
                        dataBuf.clear()
                        return dataBufOut
                    }
                }
            }
        }
        return null
    }
}