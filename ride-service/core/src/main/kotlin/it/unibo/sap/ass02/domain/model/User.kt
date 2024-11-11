package it.unibo.sap.ass02.domain.model

import it.unibo.sap.ass02.domain.ddd.Entity

interface User : Entity<Int> {
    val credit: Int
    fun decreaseCredit(amount: Int)
}
