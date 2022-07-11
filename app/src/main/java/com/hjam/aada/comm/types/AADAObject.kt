package com.hjam.aada.comm.types

open class AADAObject(private val aadaTag: Int, tagPrefix: String) {
    val screenTag = screenTag(tagPrefix, aadaTag)
    public enum class ScreenIDs {
        reserve0,
        reserve1,
        reserve2,
        reserve3,
        reserve4,
        reserve5,
        button,
        label,
        gauge1,
        gauge2,
        knob
    }
    companion object {
        private fun screenTag(tagPrefix: String, tag: Int): String {
            return tagPrefix + tag.toString()
        }
    }
}