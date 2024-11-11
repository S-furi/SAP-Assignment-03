package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.ddd.Entity
import it.unibo.sap.ass02.domain.model.EBike
import it.unibo.sap.ass02.domain.model.User
import java.time.LocalDate
import java.util.Date

interface Ride : Entity<Int> {
    val ebike: EBike
    val user: User
    val startedDate: LocalDate
    val endDate: LocalDate?

    fun end()
}
