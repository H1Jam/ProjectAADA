package com.hjam.aada.comm.types

import com.hjam.aada.ScreenObjects

class TextLabel(
    val tag: Int,
    private var x: Int?,
    private var y: Int?,
    private var size: Int?,
    private var value: String?
) {
    companion object{
        const val mID : Byte = 16.toByte()
        val typeArrayList = arrayOf(Long, Float, Int)

    }

    fun setFromInstance(textLabel:TextLabel){
        if (this.tag == textLabel.tag){
            if (textLabel.x != null) this.x = textLabel.x
            if (textLabel.y != null) this.y = textLabel.y
            if (textLabel.size != null) this.size = textLabel.size
            if (textLabel.value != null) this.value = textLabel.value
        }
    }

    fun isReady():Boolean{
        return (this.tag>0) && (this.x != null) && (this.y != null)
                && (this.size != null) && (this.value != null)
    }

    override fun toString(): String {
        return "TextLabel:[Tag:$tag, x:$x, y:$y, size:$size, value:$value]"
    }
}