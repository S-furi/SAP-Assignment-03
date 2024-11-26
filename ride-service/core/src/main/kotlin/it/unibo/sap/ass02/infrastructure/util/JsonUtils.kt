package it.unibo.sap.ass02.infrastructure.util

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets

object JsonUtils {
    val ignoreKeyJson =
        Json {
            ignoreUnknownKeys = true
        }

    suspend inline fun <reified T> decodeHttpPayload(res: HttpResponse): T? =
        runCatching {
            res.takeIf { it.status.value in 200..299 }?.let {
                ignoreKeyJson.decodeFromString<T>(it.bodyAsText(StandardCharsets.UTF_8))
            }
        }.getOrNull()
}
