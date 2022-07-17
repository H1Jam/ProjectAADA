package com.hjam.aada.comm.types

open class AADAObject(private val aadaTag: Int, tagPrefix: String) {
    val screenTag = screenTag(tagPrefix, aadaTag)
    //There will be more objects soon.
    enum class ScreenIDs {
        Reserve0,
        Reserve1,
        Reserve2,
        Reserve3,
        Reserve4,
        Reserve5,
        Button,
        Label,
        Gauge1,
        Gauge2,
        Knob,
        Map,
        MapMarkers
    }

    companion object {
        private fun screenTag(tagPrefix: String, tag: Int): String {
            return tagPrefix + tag.toString()
        }
    }
}