package com.hjam.aada.comm.types

import android.graphics.Color
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class TextLabel(
    var x: Int,
    var y: Int,
    val tag: Int,
    var text: String,
    var fontSize: Int,
    var textColor: Int
) {

    companion object {
        const val mID: Byte = 17.toByte()
        val typeArrayList = arrayOf(Long, Float, Int)
        fun fromByteBuffer(byteBuffer: ByteBuffer): TextLabel {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val fSize = byteBuffer.short.toInt()
            val textColor: Int = byteBuffer.int
            val vText = StandardCharsets.UTF_8.decode(byteBuffer).toString()
            return TextLabel(x, y, cTag, vText, fSize, textColor)
        }
    }

    fun updateFromInstance(textLabel: TextLabel) {
        if (this.tag == textLabel.tag) {
            this.x = textLabel.x
            this.y = textLabel.y
            this.fontSize = textLabel.fontSize
            this.text = textLabel.text
        }
    }

    override fun toString(): String {
        return "TextLabel:[Tag:$tag, x:$x, y:$y, FontSize:$fontSize," +
                " Text:$text, Color:${textColor?.toString(16)}]"
    }

}