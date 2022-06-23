package com.hjam.aada.comm

import com.hjam.aada.comm.types.DataStateMachine
import com.hjam.aada.utils.Crc16
import com.hjam.aada.utils.Logger
import java.nio.ByteBuffer

object DataProtocol {
    private const val mTag = "AADA_DataProtocol"
    private const val mMaxDataLength = 253 // 253 + 2(Bytes for CRC) = 255
    private const val mMinFrameLength = 4
    private val mDataBuf: ArrayList<Byte> = arrayListOf()
    private var mDataBufOut = byteArrayOf()
    private val mDataHeader = byteArrayOf(199.toByte(), 201.toByte(), 176.toByte())
    private val mDataIn: ByteArray = byteArrayOf(0, 0, 0)
    private var mDataStep = 0
    private var mDataLength = 0
    private var mLastDigitTimeStamp=0L
    private enum class DataDirection{
        ToAndroid,
        FromAndroid
    }

    /**
     * A byte receive callback. When a byte appears in the stream this method will be invoked.
     * The method runs on Bluetooth thread. Do not update UI here!
     * @param inp: an Int from input stream.
     * @return ArrayList<Byte> if the packed was parsed otherwise returns null.
     */
    fun parseIt(inp: Int): ByteArray? {
        if (isTimedOut()){
            mDataIn.fill(0)
            mDataStep=0
        }
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

    /**
     * A method to re-sync the frames. It calculates the time between last synced frame.
     * @return boolean
     */
    private fun isTimedOut():Boolean{
        if ((System.currentTimeMillis() - mLastDigitTimeStamp)>100){
            mLastDigitTimeStamp = System.currentTimeMillis()
            return true
        }
        return false
    }

    private fun checkCRC(frameBytes: ByteArray): ByteArray? {
        //Drop the Direction Byte (first one) and the received CRC16 (last two).
        val recalculatedCrc = Crc16.crc16(frameBytes.dropLast(2).drop(1).toByteArray())
        val receivedCRC = (frameBytes.takeLast(2)).toByteArray()
        return if (recalculatedCrc.contentEquals(receivedCRC)) {
            mLastDigitTimeStamp = System.currentTimeMillis()
            Logger.debug(mTag,"Good CRC! Expected:[${recalculatedCrc.joinToString()}]" +
                    " Received:[${receivedCRC.joinToString()}]")
            frameBytes.dropLast(2).toByteArray()
        } else {
            Logger.error(mTag,"Bad CRC! Expected:" +
                    "${recalculatedCrc.map { it.toUByte() }.joinToString()}," +
                    " Received:${receivedCRC.map { it.toUByte() }.joinToString()}")
            null
        }
    }

    fun prepareFrame(frameBytes: ByteArray): ByteArray? {
        Logger.debug(mTag, "sendFrame: ${(frameBytes+Crc16.crc16(frameBytes)).map { it.toUByte() }.joinToString()}")
        if (frameBytes.size > mMaxDataLength){
            return null
        }
        return mDataHeader +
                byteArrayOf((frameBytes.size + 3).toByte(),
                    DataDirection.FromAndroid.ordinal.toByte()) +
                frameBytes + Crc16.crc16(frameBytes)
    }


    fun handleData(frameBytes: ByteArray){
        Logger.debug(mTag,"handleData!")
        if (frameBytes.size > mMinFrameLength && frameBytes[0] == DataDirection.ToAndroid.ordinal.toByte()){
            dispatchData(frameBytes)
        }
    }

    private fun dispatchData(frameBytes: ByteArray){
        Logger.debug(mTag,"dispatchData!")
        val bb =  ByteBuffer.wrap(frameBytes)
        bb.get() // To remove the first byes, the direction byte.
        DataStateMachine.process(bb)
    }
}
