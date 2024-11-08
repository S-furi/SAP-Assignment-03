package it.unibo.sap.ass02.domain.model

interface V2d {
    val x: Int
    val y: Int

    fun rotate(d: Double): V2d

    operator fun plus(other: V2d) = fromCoord(x + other.x, y + other.y)

    operator fun times(p: Int) = fromCoord(x * p, y * p)

    companion object {
        fun fromCoord(
            x: Int,
            y: Int,
        ) = object : V2d {
            override val x: Int = x
            override val y: Int = y

            override fun rotate(d: Double): V2d {
                TODO("Not yet implemented")
            }
        }
    }
}
