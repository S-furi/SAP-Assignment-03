package it.unibo.sap.ass02.domain

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class UserImpl(
    override val id: Int,
    @EncodeDefault override var credit: Int = 100
) : User {

    override fun rechargeCredit(deltaCredit: Int) {
        require(deltaCredit > 0) { "Credit must be a positive integer." }
        this.credit += deltaCredit
    }

    override fun decreaseCredit(amount: Int) {
        require(amount > 0) { "Credit must be a positive integer." }
        require(amount <= this.credit) { "The requested amount $amount is too much for credit: ${this.credit}" }
        this.credit -= amount
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserImpl

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
