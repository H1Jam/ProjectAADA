package com.hjam.aada.comm

import com.hjam.aada.utils.Crc16
import com.hjam.aada.utils.Logger

object DataProtocol {
    private const val mTag = "AADA_DataProtocol"
    private const val mMaxDataLength = 253 // 253 + 2(Bytes for CRC) = 255
    private val mDataBuf: ArrayList<Byte> = arrayListOf()
    private var mDataBufOut = byteArrayOf()
    private val mDataHeader = byteArrayOf(199.toByte(), 201.toByte(), 176.toByte())
    private val mDataIn: ByteArray = byteArrayOf(0, 0, 0)
    private var mDataStep = 0
    private var mDataLength = 0

    /**
     * A byte receive callback. When a byte appears in the stream this method will be invoked.
     * The method runs on Bluetooth thread. Do not update UI here!
     * @param inp: an Int from input stream.
     * @return ArrayList<Byte> if the packed was parsed otherwise returns null.
     */
    fun parseIt(inp: Int): ByteArray? {
        when (mDataStep) {
            0 -> {
                mDataIn[0] = mDataIn[1]
                mDataIn[1] = mDataIn[2]
                mDataIn[2] = inp.toByte()
                if (mDataIn.contentEquals(mDataHeader)) {
                    Logger.debug("parseIt", "Got the header!")
                    mDataIn.fill(0)
                    mDataStep = 1
                }
            }
            1 -> {
                mDataLength = inp.toUByte().toInt()
                mDataStep = if (mDataLength > 0) 2 else 0
                Logger.debug("parseIt", "Got the Length:[$mDataLength]")
            }
            2 -> {
                mDataLength--
                mDataBuf.add(inp.toByte())
                if (mDataLength < 1) {
                    mDataStep = 0
                    Logger.debug("parseIt", "Got the frame:[${mDataBuf.toByteArray().toUByteArray().joinToString()}]")
                    if (mDataBuf.size > 0) {
                        mDataBufOut = mDataBuf.toMutableList().toByteArray()
                        mDataBuf.clear()
                        return checkCRC(mDataBufOut)
                    }
                }
            }
        }
        return null
    }

    private fun checkCRC(frameBytes: ByteArray): ByteArray? {
        val recalculatedCrc = Crc16.crc16(frameBytes.dropLast(2).toByteArray())
        val receivedCRC = (frameBytes.takeLast(2)).toByteArray()
        return if (recalculatedCrc.contentEquals(receivedCRC)) {
            Logger.debug(mTag,"Good CRC! Expected:[${recalculatedCrc.joinToString()}]" +
                    " Received:[${receivedCRC.joinToString()}]")
            frameBytes.dropLast(2).toByteArray()
        } else {
            Logger.error(mTag,"Bad CRC! Expected:${recalculatedCrc.joinToString()}," +
                    " Received:${receivedCRC.joinToString()}")
            null
        }
    }

    fun prepareFrame(frameBytes: ByteArray): ByteArray? {
        Logger.debug(mTag, "sendFrame: ${frameBytes.map { it.toUByte() }.joinToString()}")
        if (frameBytes.size > mMaxDataLength){
            return null
        }
        return mDataHeader + byteArrayOf((frameBytes.size + 2).toByte()) + frameBytes +
                Crc16.crc16(frameBytes)
    }
}