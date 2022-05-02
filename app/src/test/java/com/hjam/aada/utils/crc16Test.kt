package com.hjam.aada.utils

import junit.framework.TestCase

class Crc16Test : TestCase() {

    fun testInit() {
        val inp = listOf(49,50,51,52,53,54,55,56,57).map(Int::toUByte).toUByteArray()
        val crcActual = Crc16.crc16(inp)
        val expected = (0xE5CC).toUShort()
        assertEquals(expected,crcActual)
    }
}