package com.hjam.aada.comm

import junit.framework.TestCase
import org.junit.Test

//Todo: Assert Null on each that must be null and non-null on the one that shouldn't be null.
class DataProtocolTest : TestCase() {

    private fun assertData(data : ByteArray?): ByteArray? {
        if (data == null){
            return null
        }
        for (d in data.dropLast(1)){
            assertNull(DataProtocol.parseIt(d.toInt()))
        }
        return DataProtocol.parseIt(data.last().toInt())
    }

    private fun assertData(data : Array<Int>): ByteArray?{
        return assertData(data.map{it.toByte()}.toByteArray())
    }

    @Test
    fun testParseIt() {
        val goodData1 = byteArrayOf(6,7,8,9,10,11,12,13,14,15,16,17)
        val goodData2 = byteArrayOf(128.toByte())// single member!
        // Has the header sequence in the data frame
        val criticalData = byteArrayOf(6,7,8,9,199.toByte(),201.toByte(),176.toByte(),13,14,15,16,17)
        val goodFrame1 =  DataProtocol.prepareFrame(goodData1)
        val goodFrame2 =  DataProtocol.prepareFrame(goodData2)
        val criticalFrame =  DataProtocol.prepareFrame(criticalData)
        val badFrame1 = arrayOf(199,201,176,4,33,66,77,99)

        // Some random data to simulate noise or residues.
        assertNull(DataProtocol.parseIt(10))
        assertNull(DataProtocol.parseIt(12))
        assertNull(DataProtocol.parseIt(13))
        assertNull(DataProtocol.parseIt(14))
        assertNull(DataProtocol.parseIt(16))
        assertTrue(assertData(goodFrame1).contentEquals(goodData1))
        assertNull(assertData(badFrame1)) // Bad CRC!
        assertTrue(assertData(criticalFrame).contentEquals(criticalData))
        assertTrue(assertData(goodFrame2).contentEquals(goodData2))
        assertNull(DataProtocol.parseIt(14))
        assertNull(DataProtocol.parseIt(16))
        // Length = 0!
        assertNull(DataProtocol.parseIt(199))
        assertNull(DataProtocol.parseIt(201))
        assertNull(DataProtocol.parseIt(176))
        assertNull(DataProtocol.parseIt(0))
        assertNull(DataProtocol.parseIt(21))

        val goodDataLongArray : ArrayList<Byte> = arrayListOf()
        for (i in 0..255) {
            goodDataLongArray.add(i.toByte())
        }
        val tooDataLong = goodDataLongArray.toByteArray()
        val goodDataLong = goodDataLongArray.dropLast(3).toByteArray()
        val longFrame = DataProtocol.prepareFrame(goodDataLong)
        assertNull(DataProtocol.prepareFrame(tooDataLong))
        assertNotNull(longFrame)
        assertTrue(assertData(longFrame).contentEquals(goodDataLong))
        assertTrue(assertData(goodFrame2).contentEquals(goodData2))
    }
}