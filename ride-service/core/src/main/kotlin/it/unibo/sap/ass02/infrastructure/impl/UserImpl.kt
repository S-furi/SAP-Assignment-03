package it.unibo.sap.ass02.infrastructure.impl

import it.unibo.sap.ass02.domain.model.User
import it.unibo.sap.ass02.infrastructure.stub.UserStub

data class UserImpl(
    override val id: Int,
) : User {
    override val credit: Int
        get() = UserStub.getUser(this.id)?.credit ?: -1

    override fun increaseCredit(amount: Int) {
        UserStub.increaseCredit(this.id, amount)
    }

    override fun decreaseCredit(amount: Int) {
        UserStub.decreaseCredit(this.id, amount)
    }
}
