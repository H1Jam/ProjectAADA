package com.hjam.aada.comm.types

open class AADAObject(private val aadaTag: Int, private val mTagPrefix: String, val mID: Byte) {
    val screenTag = screenTag(mTagPrefix ,aadaTag)
    public enum class ScreenIDs{
        button,
        label,
        knob
    }
    companion object {
       private fun screenTag(tagPrefix: String, tag: Int): String {
            return tagPrefix + tag.toString()
        }
    }
}