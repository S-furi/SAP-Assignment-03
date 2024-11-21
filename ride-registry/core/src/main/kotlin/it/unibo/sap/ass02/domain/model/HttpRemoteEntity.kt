package it.unibo.sap.ass02.domain.model

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking

object HttpRemoteEntity {
    private val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

    fun checkId(findEntityUri: String) =
        runBlocking {
            runCatching {
                client.get(findEntityUri).status.value in (200..299)
            }.getOrDefault(false)
        }
}
