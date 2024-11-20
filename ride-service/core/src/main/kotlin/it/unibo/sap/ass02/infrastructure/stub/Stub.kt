package it.unibo.sap.ass02.infrastructure.stub

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

sealed class Stub(
    val HEALTHCHECK_URL: String,
    val GATEWAY_HOST: String = System.getenv("GATEWAY_HOST") ?: "localhost",
    val GATEWAY_PORT: String = System.getenv("GATEWAY_PORT") ?: "1926",
) {
    protected val logger = LoggerFactory.getLogger(Stub::class.java)

    val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

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
