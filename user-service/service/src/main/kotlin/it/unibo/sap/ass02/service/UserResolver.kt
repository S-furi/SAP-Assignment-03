package it.unibo.sap.ass02.service

import it.unibo.sap.ass02.persistence.UserRepository
import org.slf4j.LoggerFactory

typealias UserEvent = Pair<Int, Int>

object UserResolver {
    private val repo = UserRepository()
    private val logger = LoggerFactory.getLogger(UserResolver::class.java)

    suspend fun handleEvent(event: UserEvent) {
        val userId = event.first
        val amount = event.second
        runCatching {
            repo.findById(userId)?.let {
                when (amount >= 0) {
                    true -> it.rechargeCredit(amount)
                    else -> it.decreaseCredit(amount)
                }
            }
        }.onFailure {
            logger.warn(it.message)
        }
    }
}
