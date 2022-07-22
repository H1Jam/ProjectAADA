package com.hjam.aada.comm.types

import java.nio.ByteBuffer
import java.nio.ByteOrder

class AADASeekBar(
    var x: Int,
    var y: Int,
    val tag: Int,
    val cmdId: Int,
    var seekValue : Int = 0,
    var maxValue : Int = 0,
    val width: Int,
) : AADAObject(tag, "skb") {
    companion object {
        val objID = ScreenIDs.SeekBar.ordinal.toByte()
        const val minCallTime = 50
        fun fromByteBuffer(byteBuffer: ByteBuffer): AADASeekBar {
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            val x = byteBuffer.short.toInt()
            val y = byteBuffer.short.toInt()
            val cTag = byteBuffer.short.toInt()
            val cmdId = byteBuffer.get().toInt()
            val seekValue = byteBuffer.short.toInt()
            val maxValue = byteBuffer.short.toInt()
            val width = byteBuffer.short.toInt()
            return AADASeekBar(x, y, cTag, cmdId, seekValue, maxValue, width)
        }

        fun toBytesFromTag(aadaSeekBar: AADASeekBar): ByteArray? {
            val bb1 = ByteBuffer.allocate(Byte.SIZE_BYTES + Short.SIZE_BYTES + Short.SIZE_BYTES)
            bb1.order(ByteOrder.LITTLE_ENDIAN)
            val array = with(bb1) {
                put(objID)
                putShort(aadaSeekBar.tag.toShort())
                putShort((aadaSeekBar.seekValue.coerceIn(0,aadaSeekBar.maxValue)).toShort())
            }.array()
            return array
        }
    }

    fun setSeek(value : Int){
        this.seekValue = value.coerceIn(0,this.maxValue)
    }

    override fun toString(): String {
        return "AADASeekBar:[Tag:$tag($screenTag), cmdId:$cmdId, seekValue:$seekValue, " +
                "x:$x, y:$y, width:$width, maxValue:$maxValue]"
    }
}
