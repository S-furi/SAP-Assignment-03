package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.ddd.Entity
import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.User
import java.time.LocalDate

interface Ride : Entity<Int> {
    val ebike: EBike
    val user: User
    var endDate: LocalDate?
    var startedDate: LocalDate?

    fun start()

    fun end()
}
