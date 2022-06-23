package com.hjam.aada

import android.graphics.Color
import com.hjam.aada.comm.types.AADATextLabel
import com.hjam.aada.utils.Logger
import junit.framework.TestCase
import org.junit.Test


class ScreenObjectsTest : TestCase() {
    @Test
    fun testAddTextLabel() {
        Logger.debug("testAddTextLabel", "-----------------------------------")
        ScreenObjects.addTextLabel(AADATextLabel(1, 10, 20, "TEXTTEST!!", 30, Color.RED))
        ScreenObjects.addTextLabel(AADATextLabel(2, 10, 20, "TEXTTEST!2!", 30, Color.BLUE))
        ScreenObjects.addTextLabel(AADATextLabel(3, 10, 20, "TEXTTEST!3!", 30, Color.BLACK))
    }
}