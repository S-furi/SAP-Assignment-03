package it.unibo.sap.ass02.service

import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.persistence.UserCreditEventImpl
import it.unibo.sap.ass02.persistence.UserRepository
import it.unibo.sap.ass02.persistence.UserRepositoryImpl
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

typealias UserEvent = Triple<Int, Int, LocalDateTime>

object UserResolver {
    private val repo: UserRepository = UserRepositoryImpl()
    private val logger = LoggerFactory.getLogger(UserResolver::class.java)

    fun handleEvent(event: UserEvent) {
        logger.info("Serving request for userId=${event.first}, amount=${event.second}")
        repo.appendEvent(UserCreditEventImpl(event))
        runCatching {
            repo.collectUser(event.first)?.also { logger.info("Got user $it") }
        }.onFailure { logger.warn(it.message) }
    }

    fun getUser(userId: Int): User? = repo.collectUser(userId)
}
