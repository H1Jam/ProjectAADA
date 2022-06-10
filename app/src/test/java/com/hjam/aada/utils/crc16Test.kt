package com.hjam.aada.utils

import junit.framework.TestCase
import org.junit.Test

class Crc16Test : TestCase() {

    @Test
    fun test_CRC_to_array() {
        val inp = byteArrayOf(49,50,51,52,53,54,55,56,57)
        val crcActual = Crc16.crc16(inp)
        val expected = byteArrayOf(0xE5.toByte(), 0xCC.toByte())
        assertTrue(expected.contentEquals(crcActual))
    }

}