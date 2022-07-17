package com.hjam.aada.comm.types

class AADAMap(
    val x: Int,
    val y: Int,
    val height: Int,
    val width: Int,
    val tag: Int
) : AADAObject(tag, "map")  {
    val objID = ScreenIDs.Map.ordinal.toByte()


    override fun toString(): String {
        return "AADAMap:[Tag:$tag (${screenTag}), x:$x, y:$y, height:$height, width:$width]"
    }
}