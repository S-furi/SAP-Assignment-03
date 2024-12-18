package it.unibo.sap.ass02.persistence

import it.unibo.sap.ass02.domain.User

interface UserRepository {
    fun appendEvent(event: UserCreditEvent)
    fun collectUser(userId: Int): User?
}
