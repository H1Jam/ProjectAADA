package com.hjam.aada.comm.types

import com.hjam.aada.utils.Logger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADASwitch(
    var x: Int,
    var y: Int,
    val tag: Int,
    var fontSize: Int,
    var textColor: Int,
    var text: String
) : AADAObject(tag, "swc") {
    var value = false
    companion object {
        val objID = ScreenIDs.ToggleSwitch.ordinal.toByte()
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADASwitch {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val fSize = byteBuffer.short.toInt()
            val textColor: Int = byteBuffer.int
            val vText = StandardCharsets.UTF_8.decode(byteBuffer).toString()
            return AADASwitch(x, y, cTag, fSize, textColor, vText)
        }

        fun toBytesFromTag(aadaSwitch: AADASwitch): ByteArray? {
            val bb1 = ByteBuffer.allocate(Byte.SIZE_BYTES + Short.SIZE_BYTES + Byte.SIZE_BYTES)
            bb1.order(ByteOrder.LITTLE_ENDIAN)
            val array = with(bb1) {
                put(objID)
                putShort(aadaSwitch.tag.toShort())
                put(if(aadaSwitch.value) 1 else 0)
            }.array()
            return array
        }
    }
    override fun toString(): String {
        return "AADAButton:[Tag:$tag($screenTag), x:$x, y:$y Text:$text, FontSize:$fontSize, " +
                "textColor:${textColor.toString(16)}]"
    }

}
