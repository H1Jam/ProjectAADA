package com.hjam.aada.comm.types

import com.hjam.aada.ScreenObjects
import com.hjam.aada.utils.Logger
import java.nio.ByteBuffer

class DataStateMachine {
    companion object{
        val mTag = "DataStateMachine"
        var switch :Byte = 0
        fun process(bb: ByteBuffer){
            Logger.debug(mTag,"DataStateMachine.process!")
            switch = bb.get()
            Logger.debug(mTag,"DataStateMachine.process! mID:$switch")
            when (switch){
                AnalogGauge.mID->{
                    Logger.debug(mTag, "Got An alogGauge")
                    //val s: String = StandardCharsets.UTF_8.decode(bb).toString()
                }
                AADATextLabel.mID->{
                    val textLabel = AADATextLabel.fromByteBuffer(bb)
                    Logger.debug(mTag, "Got a TextLabel $textLabel")
                    ScreenObjects.addTextLabel(textLabel)
                }
                AADAButton.mID->{
                    val aadaButton = AADAButton.fromByteBuffer(bb)
                    Logger.debug(mTag, "Got a AADAButton $aadaButton")
                    ScreenObjects.addButton(aadaButton)
                }

                200.toByte()->{
                    Logger.debug(mTag, "Got 200")
                }
            }
        }
    }
}
