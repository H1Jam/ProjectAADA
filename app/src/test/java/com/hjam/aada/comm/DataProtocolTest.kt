package com.hjam.aada.comm

import junit.framework.TestCase

//Todo: Assert Null on each that must be null and non-null on the one that shouldn't be null.
class DataProtocolTest : TestCase() {

    fun testParseIt() {
        assertNull(DataProtocol.parseIt(10))
        assertNull(DataProtocol.parseIt(12))
        assertNull(DataProtocol.parseIt(13))
        assertNull(DataProtocol.parseIt(14))
        assertNull(DataProtocol.parseIt(16))
        assertNull(DataProtocol.parseIt(199))
        assertNull(DataProtocol.parseIt(201))
        assertNull(DataProtocol.parseIt(176))
        assertNull(DataProtocol.parseIt(3))
        assertNull(DataProtocol.parseIt(6))
        assertNull(DataProtocol.parseIt(7))
        assertNotNull(DataProtocol.parseIt(8))

        assertNull(DataProtocol.parseIt(199))
        assertNull(DataProtocol.parseIt(201))
        assertNull(DataProtocol.parseIt(176))
        assertNull(DataProtocol.parseIt(1))
        assertNotNull(DataProtocol.parseIt(128))

        assertNull(DataProtocol.parseIt(199))
        assertNull(DataProtocol.parseIt(201))
        assertNull(DataProtocol.parseIt(176))
        assertNull(DataProtocol.parseIt(0))
        assertNull(DataProtocol.parseIt(21))

        assertNull(DataProtocol.parseIt(199))
        assertNull(DataProtocol.parseIt(199))
        assertNull(DataProtocol.parseIt(201))
        assertNull(DataProtocol.parseIt(176))
        assertNull(DataProtocol.parseIt(2))
        assertNull(DataProtocol.parseIt(33))
        val a2 = DataProtocol.parseIt(33)
        assertNotNull(a2)
        println(a2)

        assertNull(DataProtocol.parseIt(199))
        assertNull(DataProtocol.parseIt(201))
        assertNull(DataProtocol.parseIt(176))
        assertNull(DataProtocol.parseIt(3))
        assertNull(DataProtocol.parseIt(199))
        assertNull(DataProtocol.parseIt(201))
        assertNotNull(DataProtocol.parseIt(176))
        assertNull(DataProtocol.parseIt(1))
        assertNull(DataProtocol.parseIt(33))

        assertNull(DataProtocol.parseIt(199))
        assertNull(DataProtocol.parseIt(201))
        assertNull(DataProtocol.parseIt(176))
        assertNull(DataProtocol.parseIt(256))
        for (i in 0..255) {
            val a = DataProtocol.parseIt(i)
            if (a != null) {
                println(a)
            }
        }
    }
}