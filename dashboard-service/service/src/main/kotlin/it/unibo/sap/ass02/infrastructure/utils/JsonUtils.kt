package it.unibo.sap.ass02.infrastructure.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object JsonUtils {
    val logger: Logger = LoggerFactory.getLogger(JsonUtils::class.java)

    val customJson =
        Json {
            ignoreUnknownKeys = true
        }

    suspend inline fun <reified T> decodeHttpPayload(res: HttpResponse): T? =
        runCatching {
            res
                .takeIf { it.status.value in 200..299 }
                ?.let {
                    customJson.decodeFromString<T>(it.bodyAsText(StandardCharsets.UTF_8))
                }.also { if (it == null) logger.error("Got a status code different than [200-299]") }
        }.onFailure { logger.error("An error occurred: ${it.message} \n${it.stackTraceToString()}") }.getOrNull()

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = LocalDate::class)
    object LocalDateSerializer : KSerializer<LocalDate?> {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        override fun serialize(
            encoder: Encoder,
            value: LocalDate?,
        ) {
            if (value != null) encoder.encodeString(value.format(formatter))
        }

        override fun deserialize(decoder: Decoder): LocalDate? =
            runCatching {
                LocalDate.parse(decoder.decodeString(), formatter)
            }.getOrNull()
    }
}
