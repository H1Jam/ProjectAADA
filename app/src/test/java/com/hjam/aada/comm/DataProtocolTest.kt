package com.hjam.aada.comm

import com.hjam.aada.MainActivity
import junit.framework.TestCase

class DataProtocolTest : TestCase() {

    fun testParseIt() {
        println(DataProtocol.parseIt(10))
        println(DataProtocol.parseIt(12))
        println(DataProtocol.parseIt(13))
        println(DataProtocol.parseIt(14))
        println(DataProtocol.parseIt(16))
        println(DataProtocol.parseIt(199))
        println(DataProtocol.parseIt(201))
        println(DataProtocol.parseIt(176))
        println(DataProtocol.parseIt(3))
        println(DataProtocol.parseIt(6))
        println(DataProtocol.parseIt(7))
        println(DataProtocol.parseIt(8))
        println(DataProtocol.parseIt(199))
        println(DataProtocol.parseIt(201))
        println(DataProtocol.parseIt(176))
        println(DataProtocol.parseIt(1))
        println(DataProtocol.parseIt(128))
        println(DataProtocol.parseIt(199))
        println(DataProtocol.parseIt(201))
        println(DataProtocol.parseIt(176))
        println(DataProtocol.parseIt(0))
        println(DataProtocol.parseIt(21))
        println(DataProtocol.parseIt(199))
        println(DataProtocol.parseIt(199))
        println(DataProtocol.parseIt(201))
        println(DataProtocol.parseIt(176))
        println(DataProtocol.parseIt(2))
        println(DataProtocol.parseIt(33))
        println(DataProtocol.parseIt(33))

        println(DataProtocol.parseIt(199))
        println(DataProtocol.parseIt(201))
        println(DataProtocol.parseIt(176))
        println(DataProtocol.parseIt(3))
        println(DataProtocol.parseIt(199))
        println(DataProtocol.parseIt(201))
        println(DataProtocol.parseIt(176))
        println(DataProtocol.parseIt(1))
        println(DataProtocol.parseIt(33))

        println(DataProtocol.parseIt(199))
        println(DataProtocol.parseIt(201))
        println(DataProtocol.parseIt(176))
        println(DataProtocol.parseIt(256))
        for (i in 0..255) {
            val a = DataProtocol.parseIt(i)
            if (a != null) {
                println(a)
            }
        }


    }
}