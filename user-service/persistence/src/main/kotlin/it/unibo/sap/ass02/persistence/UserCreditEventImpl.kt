package it.unibo.sap.ass02.persistence

import java.time.LocalDateTime

data class UserCreditEventImpl(
    override val userId: Int,
    override val timestamp: LocalDateTime,
    override val creditUpdate: Int,
) : UserCreditEvent {
    companion object {
        fun of(
            userId: Int,
            creditUpdate: Int,
        ): UserCreditEvent = UserCreditEventImpl(userId, LocalDateTime.now(), creditUpdate)
    }
}
