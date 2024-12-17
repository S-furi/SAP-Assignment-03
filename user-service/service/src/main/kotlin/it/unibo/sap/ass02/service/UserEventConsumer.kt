package it.unibo.sap.ass02.service

import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A simple kafka message handler that manages incoming request for
 * credit changes of a given user.
 *
 * Default topic is `update-credit`, where the message is expected
 * to be in the form: key -> (userId: Int), value -> (amount: Int).
 */
class UserEventConsumer(
    private val host: String = System.getenv("KAFKA_HOST") ?: "localhost",
    private val port: Int = System.getenv("KAFKA_PORT")?.toInt() ?: 29092,
    private val topicName: String = System.getenv("UPDATE_USER_CREDIT_TOPIC_NAME") ?: "update-credit",
) : Closeable {
    private val logger = LoggerFactory.getLogger(UserEventConsumer::class.java)
    private val isPolling: AtomicBoolean = AtomicBoolean(false)

    private fun getConsumer() =
        KafkaConsumer<Int, Int>(
            mapOf(
                ConsumerConfig.GROUP_ID_CONFIG to "user-group-id",
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "$host:$port",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to true.toString(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
                ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG to "1000",
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to "15000",
                ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to "5000",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.IntegerDeserializer",
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.IntegerDeserializer",
            ),
        )

    suspend fun asyncUnlimitedConsume(callback: suspend (UserEvent) -> Unit) =
        withContext(Dispatchers.IO) {
            coroutineScope {
                getConsumer().use { kConsumer ->
                    kConsumer.subscribe(listOf(topicName))
                    isPolling.set(true)
                    logger.info("Listening events on: $host:$port on topic \"$topicName\"")
                    kConsumer
                        .consumeAsFlow()
                        .cancellable()
                        .collect { callback(it) }
                }
            }
        }

    private fun <K, V> KafkaConsumer<K, V>.consumeAsFlow(): Flow<Pair<K, V>> =
        flow {
            use {
                while (isPolling.get()) {
                    val records = poll(Duration.ofMillis(500))
                    if (!records.isEmpty) records.map { (it.key() to it.value()) }.forEach { emit(it) }
                }
            }
        }

    fun stopPolling() {
        this.isPolling.set(false)
    }

    override fun close() {
        this.stopPolling()
    }
}