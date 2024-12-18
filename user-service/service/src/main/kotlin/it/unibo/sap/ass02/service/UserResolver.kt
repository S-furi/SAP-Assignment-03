package it.unibo.sap.ass02.service

import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.persistence.UserCreditEventImpl
import it.unibo.sap.ass02.persistence.UserRepository
import it.unibo.sap.ass02.persistence.UserRepositoryImpl
import org.slf4j.LoggerFactory

typealias UserEvent = Pair<Int, Int>

object UserResolver {
    private val repo: UserRepository = UserRepositoryImpl()
    private val logger = LoggerFactory.getLogger(UserResolver::class.java)

    fun handleEvent(event: UserEvent) {
        repo.appendEvent(UserCreditEventImpl.of(event.first, event.second))
    }

    fun getUser(userId: Int): User? = repo.collectUser(userId)
}
