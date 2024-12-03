package it.unibo.sap.ass02.infrastructure

import io.kotest.common.runBlocking
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import it.unibo.sap.ass02.domain.EBike
import it.unibo.sap.ass02.domain.P2dImpl
import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.RideImpl
import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.infrastructure.utils.JsonUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.time.Duration.Companion.seconds
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets as ClientWebSocket

class AsyncRideServiceAPITest :
    StringSpec({
        val logger = LoggerFactory.getLogger(AsyncRideServiceAPITest::class.java)

        "`getBikes` should return a list of bikes" {
            val mockBikes =
                listOf(
                    EBike("0", P2dImpl(0.0, 0.0), true, 100),
                    EBike("1", P2dImpl(1.0, 0.0), false, 85),
                    EBike("2", P2dImpl(2.0, 0.0), true, 99),
                )
            val mockEngine =
                MockEngine { req ->
                    if (req.url.encodedPath.contains("api/bikes/all")) {
                        logger.debug("diocaro ho matchato")
                        respond(
                            content = JsonUtils.customJson.encodeToString(mockBikes),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json"),
                        )
                    } else {
                        respondError(HttpStatusCode.NotFound)
                    }
                }

            val mockClient =
                HttpClient(mockEngine) {
                    install(ClientContentNegotiation) { JsonUtils.customJson }
                }

            AsyncRideServiceAPI.setCustomHttpClient(mockClient)

            val bikes = runBlocking { AsyncRideServiceAPI.getBikes() }
            bikes shouldBe mockBikes
        }
    }) {
    private fun Application.installModule() {
        install(CORS) {
            anyHost()
        }

        install(WebSockets) {
            pingPeriod = 15.seconds
            timeout = 15
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }

        install(ContentNegotiation) {
            json()
        }
    }

    private fun Application.routingModule() {
        routing {
            webSocket("/rides/1") {
                val testRide =
                    RideImpl(
                        1,
                        EBike("uu1", P2dImpl(0.0, 0.0), true, 100),
                        User(1, 1000),
                    )
                for (i in 0..2) {
                    if (i % 2 == 0) {
                        send(testRide.copy(startedDate = LocalDate.now()).toFrame())
                    } else {
                        send(testRide.copy(endDate = LocalDate.now()).toFrame())
                    }
                }
                close()
            }
        }
    }

    private fun Ride.toFrame(): Frame.Text = Frame.Text(Json.encodeToString(this))

    @Test
    fun testWebSocket() {
        testApplication {
            application {
                installModule()
            }
            val mockClient =
                createClient {
                    install(ClientWebSocket) {
                        this.pingInterval = 5.seconds
                    }
                }

            AsyncRideServiceAPI.setCustomHttpClient(mockClient)
            ServicesRoutes.API_ENDPOINT = "://localhost:80"
            val testRide =
                RideImpl(
                    1,
                    EBike("uu1", P2dImpl(0.0, 0.0), true, 100),
                    User(1, 1000),
                )

            val flow = AsyncRideServiceAPI.subscribeToSimulation(testRide.id)

            flow.take(3).collect { it.id shouldBe testRide.id }
        }
    }
}
