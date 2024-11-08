package it.unibo.sap.ass02.domain

import it.unibo.sap.ass02.domain.ddd.Entity
import java.util.Date

interface Ride : Entity<Int> {
    val ebike: EBike
    val user: User
    val startedDate: Date
    val endDate: Date
}
