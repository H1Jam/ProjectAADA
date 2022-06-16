package com.hjam.aada

import com.hjam.aada.comm.types.TextLabel
import com.hjam.aada.utils.Logger
import junit.framework.TestCase
import org.junit.Test


class ScreenObjectsTest : TestCase() {
    @Test
    fun testAddTextLabel() {
        Logger.debug("testAddTextLabel","-----------------------------------")
        ScreenObjects.addTextLabel(TextLabel(1,10,20,30,"TEXTTEST!!"))
        ScreenObjects.addTextLabel(TextLabel(2,10,20,30,"TEXTTEST!2!"))
        ScreenObjects.addTextLabel(TextLabel(3,10,20,30,"TEXTTEST!3!"))
        ScreenObjects.addTextLabel(TextLabel(1,11,null,null,null))
        ScreenObjects.addTextLabel(TextLabel(4,10,20,30,"TEXTTEST!5"))
    }
}