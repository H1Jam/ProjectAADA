package com.hjam.aada.comm.types

import junit.framework.TestCase

class AADAKnobTest : TestCase(){
    fun test_screenTag(){
       val a = AADAKnob(10,20,30,200,0,0,43)
        assertEquals("knb43",a.screenTag)
    }
}