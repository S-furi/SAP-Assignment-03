package it.unibo.sap.ass02.infrastructure.proxies.events

import it.unibo.sap.ass02.domain.model.User

data class EventUserImpl(override val id: Int, override val credit: Int) : User {
    private val eventProducer = UserEventProducer()

    override fun increaseCredit(amount: Int) {
        eventProducer.increaseUserCredit(id, amount)
    }

    override fun decreaseCredit(amount: Int) {
        eventProducer.decreaseUserCredit(id, amount)
    }
}
