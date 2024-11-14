package it.unibo.sap.ass02.domain.model

import it.unibo.sap.ass02.domain.model.V2d.Companion.fromCoord

interface P2d {
    val x: Int
    val y: Int

    operator fun plus(other: P2d): P2d = fromCoord(x + other.x, y + other.y)

    operator fun plus(other: V2d): P2d = fromCoord((x + other.x).toInt(), (y + other.y).toInt())

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
