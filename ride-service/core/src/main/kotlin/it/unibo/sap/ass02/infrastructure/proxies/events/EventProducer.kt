package it.unibo.sap.ass02.infrastructure.proxies.events

import io.ktor.http.headers
import io.ktor.utils.io.core.Closeable
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.header.internals.RecordHeader
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Future

abstract class EventProducer(
    host: String = System.getenv("KAFKA_HOST") ?: "localhost",
    port: Int = System.getenv("KAFKA_PORT")?.toInt() ?: 29092,
) : Closeable {
    protected val defaultKafkaParameters =
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "$host:$port",
            ProducerConfig.ACKS_CONFIG to "all",
            ProducerConfig.RETRIES_CONFIG to 0,
            ProducerConfig.BATCH_SIZE_CONFIG to 16384,
            ProducerConfig.LINGER_MS_CONFIG to 1,
            ProducerConfig.BUFFER_MEMORY_CONFIG to 33554432,
        )
}

fun <K, V> KafkaProducer<K, V>.send(
    topicName: String,
    key: K,
    value: V,
): Future<RecordMetadata> {
    val record = ProducerRecord(topicName, 0, System.currentTimeMillis(), key, value, listOf())
    return send(record)
}
