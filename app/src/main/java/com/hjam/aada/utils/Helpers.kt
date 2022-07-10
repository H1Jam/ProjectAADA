package com.hjam.aada.utils

import kotlin.math.abs

class Helpers {
    companion object {
        fun wrapAng180(fAng: Float): Float {
            return if (fAng.rem(360.0f) < -180) {
                (fAng.rem(360.0f) + 180.0f).rem(360.0f) + 180.0f
            } else {
                (fAng.rem(360.0f) + 180.0f).rem(360.0f) - 180.0f
            }

        }

        fun wrapAng360(fAng: Float): Float {
            return (fAng.rem(360.0f) + 360.0f).rem(360.0f)
        }

        private const val EPSILON = 1e-12

        fun remap(
            value: Float,
            minSrc: Float, maxSrc: Float,
            minDest: Float, maxDest: Float
        ): Float {
            if (abs(maxSrc - minSrc) < EPSILON) {
                return minDest
            }
            val ratio = (maxDest - minDest) / (maxSrc - minSrc)
            return ratio * (value - minSrc) + minDest
        }

        fun remap(
            value: Int,
            minSrc: Int, maxSrc: Int,
            minDest: Int, maxDest: Int
        ): Int {
            return remap(
                value.toFloat(),
                minSrc.toFloat(),
                maxSrc.toFloat(),
                minDest.toFloat(),
                maxDest.toFloat()
            ).toInt()
        }
    }
}