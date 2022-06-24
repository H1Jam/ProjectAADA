package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADAButton(
    var x: Int,
    var y: Int,
    var tag: Int,
    var text: String,
    var fontSize: Int,
    var textColor: Int,
    var backColor: Int
) {
    val screenTag = screenTag(this)
    companion object {
        const val mID: Byte = 18.toByte()
        const val mTagPrefix = "btn"
        fun screenTag(aadaButton: AADAButton):String{
            return mTagPrefix +aadaButton.tag.toString()
        }
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADAButton {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val fSize = byteBuffer.short.toInt()
            val textColor: Int = byteBuffer.int
            val backColor: Int = byteBuffer.int
            val vText = StandardCharsets.UTF_8.decode(byteBuffer).toString()
            return AADAButton(x, y, cTag, vText, fSize, textColor, backColor)
        }

        fun toBytesFromObject(aadaButton: AADAButton): ByteArray {
            val bb1 = ByteBuffer.allocate(Byte.SIZE_BYTES + Short.SIZE_BYTES)
            bb1.order(ByteOrder.LITTLE_ENDIAN)
            val array = with(bb1) {
                put(mID)
                putShort(aadaButton.tag.toShort())
            }.array()
            return array
        }

        fun toBytesFromTag(tag: String): ByteArray? {
            if (!tag.startsWith(mTagPrefix)) {
                return null
            }
            val pTag = (Integer.parseInt(tag.drop(3))).toShort()
            val bb1 = ByteBuffer.allocate(Byte.SIZE_BYTES + Short.SIZE_BYTES)
            bb1.order(ByteOrder.LITTLE_ENDIAN)
            val array = with(bb1) {
                put(mID)
                putShort(pTag)
            }.array()
            return array
        }
    }


}