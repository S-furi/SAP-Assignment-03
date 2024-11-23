package it.unibo.sap.ass02.infrastructure.impl

import it.unibo.sap.ass02.domain.model.User
import it.unibo.sap.ass02.infrastructure.stub.UserProxy

data class UserImpl(
    override val id: Int,
) : User {
    override val credit: Int
        get() = UserProxy.getUser(this.id)?.credit ?: -1

    override fun increaseCredit(amount: Int) {
        UserProxy.increaseCredit(this.id, amount)
    }

    override fun decreaseCredit(amount: Int) {
        UserProxy.decreaseCredit(this.id, amount)
    }
}
