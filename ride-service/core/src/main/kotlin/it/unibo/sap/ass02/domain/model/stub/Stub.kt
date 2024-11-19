package it.unibo.sap.ass02.domain.model.stub

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking

sealed class Stub(
    val HEALTHCHECK_URL: String,
) {
    val GATEWAY_HOST = System.getenv("GATEWAY_HOST") ?: "localhost"
    val GATEWAY_PORT = System.getenv("GATEWAY_PORT") ?: 1926
    val client = HttpClient(CIO)

    init {
        runBlocking {
            val url = "http://$GATEWAY_HOST:$GATEWAY_PORT/$HEALTHCHECK_URL"
            val res = client.get(url)
            require(res.status.value in 200..299) {
                "Cannot establish connection to \"$url\""
            }
        }
    }
}
