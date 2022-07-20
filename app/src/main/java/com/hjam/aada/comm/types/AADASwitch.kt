package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADASwitch(
    var x: Int,
    var y: Int,
    val tag: Int,
    val cmdId: Int,
    var switchValue : Boolean = false,
    var fontSize: Int,
    var textColor: Int,
    var text: String
) : AADAObject(tag, "swc") {
    companion object {
        val objID = ScreenIDs.ToggleSwitch.ordinal.toByte()
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADASwitch {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val cmdId = byteBuffer.get().toInt()
            val switchValue = byteBuffer.get() != 0.toByte()
            val fSize = byteBuffer.short.toInt()
            val textColor: Int = byteBuffer.int
            val vText = StandardCharsets.UTF_8.decode(byteBuffer).toString()
            return AADASwitch(x, y, cTag, cmdId, switchValue, fSize, textColor, vText)
        }

        fun toBytesFromTag(aadaSwitch: AADASwitch): ByteArray? {
            val bb1 = ByteBuffer.allocate(Byte.SIZE_BYTES + Short.SIZE_BYTES + Byte.SIZE_BYTES)
            bb1.order(ByteOrder.LITTLE_ENDIAN)
            val array = with(bb1) {
                put(objID)
                putShort(aadaSwitch.tag.toShort())
                put(if(aadaSwitch.switchValue) 1 else 0)
            }.array()
            return array
        }
    }
    override fun toString(): String {
        return "AADASwitch:[Tag:$tag($screenTag), cmdId:$cmdId, switchValue:$switchValue, " +
                "x:$x, y:$y Text:$text, FontSize:$fontSize, " +
                "textColor:${textColor.toUInt().toString(16)}]"
    }

}
