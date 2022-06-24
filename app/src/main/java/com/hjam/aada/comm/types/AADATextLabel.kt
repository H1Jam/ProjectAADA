package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class AADATextLabel(
    var x: Int,
    var y: Int,
    val tag: Int,
    var text: String,
    var fontSize: Int,
    var textColor: Int
) {
    val screenTag = screenTag(this)
    companion object {
        const val mID: Byte = 17.toByte()
        const val mTagPrefix = "lbl"
        fun screenTag(aADATextLabel: AADATextLabel):String{
            return mTagPrefix+aADATextLabel.tag.toString()
        }
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

    fun updateFromInstance(aADATextLabel: AADATextLabel) {
        if (this.tag == aADATextLabel.tag) {
            this.x = aADATextLabel.x
            this.y = aADATextLabel.y
            this.fontSize = aADATextLabel.fontSize
            this.text = aADATextLabel.text
        }
    }

    override fun toString(): String {
        return "TextLabel:[Tag:$tag, x:$x, y:$y, FontSize:$fontSize," +
                " Text:$text, Color:${textColor?.toString(16)}]"
    }

}