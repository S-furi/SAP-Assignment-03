package it.unibo.sap.ass02.infrastructure.proxies

import io.ktor.client.request.get
import it.unibo.sap.ass02.domain.model.User
import it.unibo.sap.ass02.infrastructure.proxies.events.UserEventProducer
import it.unibo.sap.ass02.infrastructure.util.JsonUtils
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

object UserProxy : Proxy(
    healthcheckUri = "api/users/health",
) {
    private val USER_ENDPOINT = "http://$gatewayHost:$gatewayPort/api/users"
    private val GET_USER_BY_ID = "$USER_ENDPOINT/"
    private val eventProducer = UserEventProducer()

    fun getUser(id: Int): User? =
        runBlocking {
            val res = client.get(GET_USER_BY_ID + id)
            JsonUtils.decodeHttpPayload<UserImpl>(res)
        }

    fun increaseCredit(
        id: Int,
        amount: Int,
    ) = eventProducer.increaseUserCredit(id, amount)

    fun decreaseCredit(
        id: Int,
        amount: Int,
    ) = eventProducer.decreaseUserCredit(id, amount)

    @Serializable
    data class UserImpl(
        override val id: Int,
        override val credit: Int,
    ) : User {
        override fun increaseCredit(amount: Int) {
            increaseCredit(this.id, amount)
        }

        override fun decreaseCredit(amount: Int) {
            decreaseCredit(this.id, amount)
        }
    }
}
