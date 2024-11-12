package it.unibo.sap.ass02.domain.model.stub

import io.ktor.client.call.body
import io.ktor.client.request.get
import it.unibo.sap.ass02.domain.model.User

data object ClientStub : Stub() {
    private val USER_ENDPOINT = "http://$GATEWAY_HOST:$GATEWAY_PORT/api/users"
    private val GET_USER_BY_ID = "$USER_ENDPOINT/find/"
    private val INCREASE_CREDIT = "$USER_ENDPOINT/increase-credit"
    private val DECREASE_CREDIT = "$USER_ENDPOINT/decrease-credit"

    suspend fun getUser(id: String): User? {
        val res = client.get(GET_USER_BY_ID + id)
        if (res.status.value in 200..299) {
            return res.body()
        }
        return null
    }
}