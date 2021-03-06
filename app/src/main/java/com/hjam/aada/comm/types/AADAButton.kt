package com.hjam.aada.comm.types

import com.hjam.aada.utils.Logger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADAButton(
    var x: Int,
    var y: Int,
    val tag: Int,
    val cmdId: Int,
    var text: String,
    var fontSize: Int,
    var textColor: Int,
    var backColor: Int
): AADAObject(tag, "btn") {
    companion object {
        val objID = ScreenIDs.Button.ordinal.toByte()
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADAButton {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val cmdId = byteBuffer.get().toInt()
            val fSize = byteBuffer.short.toInt()
            val textColor: Int = byteBuffer.int
            val backColor: Int = byteBuffer.int
            val vText = StandardCharsets.UTF_8.decode(byteBuffer).toString()
            return AADAButton(x, y, cTag, cmdId, vText, fSize, textColor, backColor)
        }

        fun toBytesFromObject(aadaButton: AADAButton): ByteArray {
            val bb1 = ByteBuffer.allocate(Byte.SIZE_BYTES + Short.SIZE_BYTES)
            bb1.order(ByteOrder.LITTLE_ENDIAN)
            val array = with(bb1) {
                put(objID)
                putShort(aadaButton.tag.toShort())
            }.array()
            return array
        }

        fun toBytesFromTag(tag: String): ByteArray? {
            if (!tag.startsWith("btn")) {
                Logger.debug("AADAButton", "toBytesFromTag: Null!")
                return null
            }
            Logger.debug("AADAButton", "toBytesFromTag: [$tag]")
            val pTag = (Integer.parseInt(tag.drop(3))).toShort()
            val bb1 = ByteBuffer.allocate(Byte.SIZE_BYTES + Short.SIZE_BYTES)
            bb1.order(ByteOrder.LITTLE_ENDIAN)
            val array = with(bb1) {
                put(objID)
                putShort(pTag)
            }.array()
            return array
        }
    }

    override fun toString(): String {
        return "AADAButton:[Tag:$tag, x:$x, y:$y, FontSize:$fontSize," +
                " Text:$text, textColor:${textColor.toString(16)}," +
                "backColor:${backColor.toString(16)}]"
    }

}