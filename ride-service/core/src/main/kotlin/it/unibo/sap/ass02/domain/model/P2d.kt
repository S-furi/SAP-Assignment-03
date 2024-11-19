package it.unibo.sap.ass02.domain.model

interface P2d {
    val x: Double
    val y: Double

    operator fun plus(other: P2d): P2d = fromCoord(x + other.x, y + other.y)

    operator fun plus(other: V2d): P2d = fromCoord(x + other.x, y + other.y)

    companion object {
        fun fromCoord(
            x: Double,
            y: Double,
        ) = object : P2d {
            override val x: Double = x
            override val y: Double = y
        }
    }
}
