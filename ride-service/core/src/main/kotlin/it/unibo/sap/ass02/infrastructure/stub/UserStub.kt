package it.unibo.sap.ass02.infrastructure.stub

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import it.unibo.sap.ass02.domain.model.User
import kotlinx.coroutines.runBlocking

data object UserStub : Stub(
    HEALTHCHECK_URL = "api/users/health",
    GATEWAY_PORT = "11000"
) {
    private val USER_ENDPOINT = "http://$GATEWAY_HOST:$GATEWAY_PORT/api/users"
    private val GET_USER_BY_ID = "$USER_ENDPOINT/find/"
    private val INCREASE_CREDIT = "$USER_ENDPOINT/increase-credit"
    private val DECREASE_CREDIT = "$USER_ENDPOINT/decrease-credit"

    fun getUser(id: Int): User? =
        runBlocking {
            val res = client.get(GET_USER_BY_ID + id)
            if (res.status.value in 200..299) res.body() else null
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
}
