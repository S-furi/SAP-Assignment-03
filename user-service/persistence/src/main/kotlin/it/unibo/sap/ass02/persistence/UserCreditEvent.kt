package it.unibo.sap.ass02.persistence

import java.time.LocalDateTime

interface UserCreditEvent {
    val userId: Int
    val timestamp: LocalDateTime
    val creditUpdate: Int
}
