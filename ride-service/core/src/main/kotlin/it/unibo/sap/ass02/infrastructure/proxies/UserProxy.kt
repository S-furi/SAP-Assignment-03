package it.unibo.sap.ass02.infrastructure.proxies

import io.ktor.client.request.get
import io.ktor.client.request.post
import it.unibo.sap.ass02.domain.model.User
import it.unibo.sap.ass02.infrastructure.util.JsonUtils
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

object UserProxy : Proxy(
    healthcheckUri = "api/users/health",
) {
    private val USER_ENDPOINT = "http://$gatewayHost:$gatewayPort/api/users"
    private val GET_USER_BY_ID = "$USER_ENDPOINT/"
    private val INCREASE_CREDIT = "$USER_ENDPOINT/increase-credit"
    private val DECREASE_CREDIT = "$USER_ENDPOINT/decrease-credit"

    fun getUser(id: Int): User? =
        runBlocking {
            val res = client.get(GET_USER_BY_ID + id)
            JsonUtils.decodeHttpPayload<UserImpl>(res)
        }

    fun increaseCredit(
        id: Int,
        amount: Int,
    ) = handleCredit(INCREASE_CREDIT, id, amount)

    fun decreaseCredit(
        id: Int,
        amount: Int,
    ) = handleCredit(DECREASE_CREDIT, id, amount)

    private fun handleCredit(
        route: String,
        id: Int,
        amount: Int,
    ) = runBlocking {
        val res =
            client.post(route) {
                url {
                    parameters.append("id", id.toString())
                    parameters.append("amount", amount.toString())
                }
            }
        res.status.value in 200..299
    }

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
