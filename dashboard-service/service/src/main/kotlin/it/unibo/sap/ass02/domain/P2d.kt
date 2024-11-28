package it.unibo.sap.ass02.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface P2d {
    val x: Double
    val y: Double
}

@Serializable
data class P2dImpl(
    override val x: Double,
    override val y: Double,
) : P2d
