package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.ddd.Entity
import kotlinx.serialization.Serializable

@Serializable
sealed interface User : Entity<Int> {
    var credit: Int

    fun rechargeCredit(deltaCredit: Int)

    fun decreaseCredit(amount: Int)
}