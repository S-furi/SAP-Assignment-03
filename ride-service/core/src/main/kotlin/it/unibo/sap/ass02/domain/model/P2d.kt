package it.unibo.sap.ass02.domain.model

interface P2d {
    val x: Int
    val y: Int

    operator fun plus(other: P2d) = fromCoord(x + other.x, y + other.y)

    operator fun plus(other: V2d) = fromCoord(x + other.x, y + other.y)

    companion object {
        fun fromCoord(
            x: Int,
            y: Int,
        ) = object : P2d {
            override val x: Int = x
            override val y: Int = y
        }
    }
}
