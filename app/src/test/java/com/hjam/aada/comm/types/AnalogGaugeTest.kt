package com.hjam.aada.comm.types

import com.hjam.aada.widgets.AnalogGauge
import junit.framework.TestCase
import org.junit.Test

class AnalogGaugeTest : TestCase(){

    @Test
    fun test_TypeList(){
        //Todo: Add the actual test!
        for (d in AnalogGauge.typeArrayList){
            println(d)
        }
    }
}