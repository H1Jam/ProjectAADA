package com.hjam.aada.comm.types

import com.hjam.aada.ScreenObjects
import com.hjam.aada.utils.Logger
import java.nio.ByteBuffer

class DataStateMachine {
    companion object{
        private const val mTag = "DataStateMachine"
        var switch :Byte = 0
        fun process(bb: ByteBuffer){
            Logger.debug(mTag,"DataStateMachine.process!")
            switch = bb.get()
            Logger.debug(mTag,"DataStateMachine.process! mID:$switch")
            when (switch){
                AADATextLabel.objID->{
                    val textLabel = AADATextLabel.fromByteBuffer(bb)
                    Logger.debug(mTag, "Got a TextLabel $textLabel")
                    ScreenObjects.addTextLabel(textLabel)
                }
                AADAButton.objID->{
                    val aadaButton = AADAButton.fromByteBuffer(bb)
                    Logger.debug(mTag, "Got a AADAButton $aadaButton")
                    ScreenObjects.addButton(aadaButton)
                }
                AADAKnob.objID->{
                    val aadaKnob = AADAKnob.fromByteBuffer(bb)
                    Logger.debug(mTag, "Got a AADAKnob $aadaKnob")
                    ScreenObjects.addKnob(aadaKnob)
                }
                AADAGauge.objID->{
                    val aadaGauge = AADAGauge.fromByteBuffer(bb)
                    Logger.debug(mTag, "Got a AADAGauge $aadaGauge")
                    ScreenObjects.addGauge(aadaGauge)
                }
                200.toByte()->{
                    Logger.debug(mTag, "Got 200")
                }
            }
        }
    }
}
