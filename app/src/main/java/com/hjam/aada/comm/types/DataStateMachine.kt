package com.hjam.aada.comm.types

import com.hjam.aada.utils.Logger
import java.nio.ByteBuffer

class DataStateMachine {
    companion object{
        val mTag = "DataStateMachine"
        var switch :Byte = 0
        fun process(bb: ByteBuffer){
            Logger.debug(mTag,"DataStateMachine.process!")
            switch = bb.get()
            Logger.debug(mTag,"DataStateMachine.process! + $switch")
            when (switch){
                AnalogGauge.mID->{
                    Logger.debug(mTag, "Got AnalogGauge")
                }
                200.toByte()->{
                    Logger.debug(mTag, "Got 200")
                }
            }
        }
    }
}
