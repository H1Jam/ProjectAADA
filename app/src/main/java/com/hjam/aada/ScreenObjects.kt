package com.hjam.aada

import com.hjam.aada.comm.types.TextLabel
import com.hjam.aada.utils.Logger

object ScreenObjects {
    private val mTag = "ScreenObjects"
    private val mTexLabelsList:MutableList<TextLabel> = mutableListOf()

    private fun refreshScreen(){
        for (tl in mTexLabelsList){
            Logger.debug(mTag,"UI: $tl")
        }
    }

    fun addTextLabel(textLabel:TextLabel){
        if (textLabel.tag <= 0){
            return
        }
        Logger.debug(mTag,"addTextLabel: ${textLabel.tag}")
        for (tl in mTexLabelsList){
            if (tl.tag == textLabel.tag){
                modifyTextLabel(textLabel)
                return
            }
        }
        mTexLabelsList.add(textLabel)
        refreshScreen()
    }

    private fun modifyTextLabel(textLabel:TextLabel){
        Logger.debug(mTag,"modifyTextLabel: ${textLabel.tag}")
        for (i in 0..mTexLabelsList.size){
            if (mTexLabelsList[i].tag == textLabel.tag){
                mTexLabelsList[i].setFromInstance(textLabel)
                refreshScreen()
                return
            }
        }
    }

    fun removeTextLabel(textLabel:TextLabel){
        for (tl in mTexLabelsList){
            if (tl.tag == textLabel.tag){
                mTexLabelsList.remove(tl)
                return
            }
        }
    }


}