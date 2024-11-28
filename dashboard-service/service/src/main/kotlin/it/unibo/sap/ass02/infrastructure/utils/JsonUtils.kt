package it.unibo.sap.ass02.infrastructure.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

object JsonUtils {
    val logger: Logger = LoggerFactory.getLogger(JsonUtils::class.java)

    val ignoreKeyJson =
        Json {
            ignoreUnknownKeys = true
        }

    suspend inline fun <reified T> decodeHttpPayload(res: HttpResponse): T? =
        runCatching {
            res
                .takeIf { it.status.value in 200..299 }
                ?.let {
                    ignoreKeyJson.decodeFromString<T>(it.bodyAsText(StandardCharsets.UTF_8))
                }.also { if (it == null) logger.error("Got a status code different than [200-299]") }
        }.onFailure { logger.error("An error occurred: ${it.message} \n${it.stackTraceToString()}") }.getOrNull()
}
