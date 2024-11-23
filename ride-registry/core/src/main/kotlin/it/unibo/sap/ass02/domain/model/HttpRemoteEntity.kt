package it.unibo.sap.ass02.domain.model

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

object HttpRemoteEntity {
    private val gatewayHost = System.getenv("GATEWAY_HOST") ?: "localhost"
    private val gatewayPort = System.getenv("GATEWAY_PORT") ?: "4001"

    private val baseUri: String = "http://$gatewayHost:$gatewayPort/api/"

    private val logger = LoggerFactory.getLogger(HttpRemoteEntity::class.java)

    private val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

    fun checkId(entityUri: String) =
        runBlocking {
            runCatching {
                logger.debug(baseUri + entityUri)
                client.get(baseUri + entityUri).status.value in (200..299)
            }.getOrDefault(false)
        }
}
