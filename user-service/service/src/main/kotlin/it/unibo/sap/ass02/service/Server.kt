package it.unibo.sap.ass02.service

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main(): Unit = runBlocking {
    val port = System.getenv("USER_SERVICE_PORT")?.toInt() ?: 11000
    val host = System.getenv("USER_SERVICE_HOST") ?: "127.0.0.1"

    embeddedServer(Netty, port = port, host = host, module = Application::module).start(wait = false)
    launch(Dispatchers.IO) {
        val consumer = UserEventConsumer()
        consumer.use {
            it.asyncUnlimitedConsume(UserResolver::handleEvent)
        }
    }
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("users/{id}") {
            call.parameters["id"]?.toInt()?.let { id ->
                UserResolver.getUser(id)?.let {
                    call.respondText(Json.encodeToString(it))
                } ?: call.respond(HttpStatusCode.NotFound, "No user was found for id: $id")
            } ?: call.respond(HttpStatusCode.BadRequest, "No id was passed as parameter!")
        }
    }
}