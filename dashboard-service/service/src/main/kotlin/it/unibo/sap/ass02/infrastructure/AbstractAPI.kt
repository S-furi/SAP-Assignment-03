package it.unibo.sap.ass02.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import it.unibo.sap.ass02.infrastructure.utils.JsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

abstract class AbstractAPI<T, C, I> {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val defaultClient =
        HttpClient(CIO) {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
            install(ContentNegotiation) {
                JsonUtils.customJson
            }
        }

    protected var client = defaultClient

    fun setCustomHttpClient(client: HttpClient) {
        this.client = client
    }

    abstract suspend fun getAll(): List<T>

    abstract suspend fun get(id: I): T?

    abstract suspend fun add(config: C): Boolean
}

object ServicesRoutes {
    private val apiHost = System.getenv("GATEWAY_HOST") ?: "localhost"
    private val apiPort = System.getenv("GATEWAY_PORT") ?: "4001"
    var API_ENDPOINT = "://$apiHost:$apiPort/api"

    val EBIKE_ROUTE = "http$API_ENDPOINT/vehicles/ebike"
    val USERS_ROUTE = "http$API_ENDPOINT/users"
    val RIDE_ROUTE = "http$API_ENDPOINT/rides"
    val RIDE_SIM_ROUTE = "ws$API_ENDPOINT/rides"
}
