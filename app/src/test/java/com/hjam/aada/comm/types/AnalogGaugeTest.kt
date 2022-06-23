package com.hjam.aada.comm.types

import junit.framework.TestCase
import org.junit.Test

class AnalogGaugeTest : TestCase(){

    @Test
    fun test_TypeList(){
        for (d in AnalogGauge.typeArrayList){
            println(d)
            if (d == Long){
                println("LOOOONG")
            }
        }
    }
}