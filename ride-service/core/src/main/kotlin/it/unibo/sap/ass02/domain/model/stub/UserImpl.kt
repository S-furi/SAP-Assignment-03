package it.unibo.sap.ass02.domain.model.stub

import it.unibo.sap.ass02.domain.model.User

data class UserImpl(
    override val id: Int,
) : User {
    override val credit: Int
        get() = TODO("Not yet implemented")

    override fun decreaseCredit(amount: Int) {
        TODO("Not yet implemented")
    }
}
