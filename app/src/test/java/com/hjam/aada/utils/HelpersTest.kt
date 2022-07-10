package com.hjam.aada.utils

import junit.framework.TestCase
import org.junit.Assert

class HelpersTest : TestCase() {

    fun test_wrapAng180() {
        println("---------- wrapAng180 ----------")
        println((-10 * 360 + 5f).rem(360))
        Assert.assertEquals(90f, Helpers.wrapAng180(90f), 0.001f)
        Assert.assertEquals(-30f, Helpers.wrapAng180(-30f), 0.001f)
        Assert.assertEquals(-30f, Helpers.wrapAng180(360f + 330f), 0.001f)
        Assert.assertEquals(-175f, Helpers.wrapAng180(185f), 0.001f)
        Assert.assertEquals(-175f, Helpers.wrapAng180(360 + 185f), 0.001f)
        Assert.assertEquals(-175f, Helpers.wrapAng180(10 * 360 + 185f), 0.001f)
        Assert.assertEquals(-175f, Helpers.wrapAng180(-10 * 360 + 185f), 0.001f)
        Assert.assertEquals(-5f, Helpers.wrapAng180(10 * 360 - 5f), 0.001f)
        Assert.assertEquals(-5f, Helpers.wrapAng180(-10 * 360 - 5f), 0.001f)
        Assert.assertEquals(-5f, Helpers.wrapAng180(-5f), 0.001f)
        Assert.assertEquals(5f, Helpers.wrapAng180(5f), 0.001f)
        Assert.assertEquals(5f, Helpers.wrapAng180(-10 * 360 + 5f), 0.001f)
        Assert.assertEquals(5f, Helpers.wrapAng180(10 * 360 + 5f), 0.001f)
    }

    fun test_wrapAng360() {
        println("---------- wrapAng360 ----------")
        Assert.assertEquals(90f, Helpers.wrapAng360(90f), 0.001f)
        Assert.assertEquals(330f, Helpers.wrapAng360(-30f), 0.001f)
        Assert.assertEquals(330f, Helpers.wrapAng360(360f + 330f), 0.001f)
        Assert.assertEquals(185f, Helpers.wrapAng360(185f), 0.001f)
        Assert.assertEquals(185f, Helpers.wrapAng360(360f + 185f), 0.001f)
        Assert.assertEquals(185f, Helpers.wrapAng360(-175f), 0.001f)
        Assert.assertEquals(175f, Helpers.wrapAng360(-185f), 0.001f)
        Assert.assertEquals(175f, Helpers.wrapAng360(-360f + -185f), 0.001f)
        Assert.assertEquals(355f, Helpers.wrapAng360(-365f), 0.001f)
        Assert.assertEquals(355f, Helpers.wrapAng360(-10 * 360f - 365f), 0.001f)
        Assert.assertEquals(355f, Helpers.wrapAng360(10 * 360f - 365f), 0.001f)
        Assert.assertEquals(355f, Helpers.wrapAng360(10 * 360f - 5f), 0.001f)
        Assert.assertEquals(5f, Helpers.wrapAng360(10 * 360f + 5f), 0.001f)
        Assert.assertEquals(5f, Helpers.wrapAng360(-10 * 360f + 5f), 0.001f)
    }

    fun test_map() {
        println("---------- map ----------")
        Assert.assertEquals(1000, Helpers.remap(100, 0, 200, 500, 1500))
        Assert.assertEquals(10, Helpers.remap(100, 0, 1000, 0, 100))
        Assert.assertEquals(-10, Helpers.remap(-100, 0, 1000, 0, 100))
        Assert.assertEquals(57.5f, Helpers.remap(150f, -1000f, 1000f, 0f, 100f), 0.0001f)
        Assert.assertEquals(-15.5f, (Helpers.remap(-155f, -1000f, 1000f, -100f, 100f)), 0.0001f)
        Assert.assertEquals(-100, (Helpers.remap(1000, -1000, 1000, 100, -100)))
    }

}