package com.hjam.aada

import android.graphics.Color
import junit.framework.TestCase

class ScreenObjectsTest : TestCase() {

    fun testMakeColorDarker() {
        val r = ScreenObjects.makeColorDarker(Color.RED)
        val g = ScreenObjects.makeColorDarker(Color.GREEN)
        val b = ScreenObjects.makeColorDarker(Color.BLUE)
        val all = ScreenObjects.makeColorDarker(Color.WHITE)
        assertEquals(0xFF4C0000.toInt(), r)
        assertEquals(0xFF004C00.toInt(), g)
        assertEquals(0xFF00004C.toInt(), b)
        assertEquals(0xFF4C4C4C.toInt(), all)
    }
}