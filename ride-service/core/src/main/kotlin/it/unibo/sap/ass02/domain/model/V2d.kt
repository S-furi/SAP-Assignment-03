package it.unibo.sap.ass02.domain.model

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

interface V2d {
    val x: Double
    val y: Double

    fun rotate(d: Double): V2d

    operator fun plus(other: V2d) = fromCoord(x + other.x, y + other.y)

    operator fun times(p: Int) = fromCoord(x * p, y * p)

    companion object {
        fun fromCoord(
            x: Double,
            y: Double,
        ) = V2dImpl(x, y)
    }

    data class V2dImpl(
        override val x: Double,
        override val y: Double,
    ) : V2d {
        override fun rotate(d: Double): V2d {
            val rad = d * Math.PI / 180
            val cs = cos(rad)
            val sn = sin(rad)
            val x1 = x * cs - y * sn
            val y1 = x * sn + y * sn
            return V2dImpl(x1, y1).normalize()
        }

        private fun normalize(): V2d {
            val module = sqrt(x * x + y * y)
            return V2dImpl(x / module, y / module)
        }
    }
}
