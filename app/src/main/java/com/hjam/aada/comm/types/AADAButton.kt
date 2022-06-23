package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADAButton(
    var  x: Int,
    var  y: Int,
    var  cTag: Int,
    var vText: String,
    var fSize: Int,
    var textColor: Int,
    var backColor: Int
) {
    companion object {
        const val mID: Byte = 18.toByte()
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADATextLabel {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val fSize = byteBuffer.short.toInt()
            val textColor: Int = byteBuffer.int
            val vText = StandardCharsets.UTF_8.decode(byteBuffer).toString()
            return AADATextLabel(x, y, cTag, vText, fSize, textColor)
    }
    }

}