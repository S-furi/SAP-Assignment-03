package it.unibo.sap.ass02.service.eventutils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
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
    private val port: Int = System.getenv("KAFKA_+PORT")?.toInt() ?: 29092,
    private val topicName: String = System.getenv("UPDATE_USER_CREDIT_TOPIC_NAME") ?: "update-credit",
) {
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

    suspend fun asyncUnlimitedConsume(callback: (Pair<Int, Int>) -> Unit) =
        withContext(Dispatchers.IO) {
            coroutineScope {
                getConsumer().use { kConsumer ->
                    kConsumer.subscribe(listOf(topicName))
                    isPolling.set(true)
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

    fun stopPolling() { this.isPolling.set(false) }
}

val producer =
    KafkaProducer<Int, Int>(
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:29092",
            ProducerConfig.ACKS_CONFIG to "all",
            ProducerConfig.RETRIES_CONFIG to 0,
            ProducerConfig.BATCH_SIZE_CONFIG to 16384,
            ProducerConfig.LINGER_MS_CONFIG to 1,
            ProducerConfig.BUFFER_MEMORY_CONFIG to 33554432,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.IntegerSerializer",
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.IntegerSerializer",
        ),
    )

fun main() =
    runBlocking {
        repeat(10) { producer.send(ProducerRecord("update-credit", it, System.currentTimeMillis().toInt())) }
        val consumer = UserEventConsumer()
        val job = launch { consumer.asyncUnlimitedConsume(::println) }
        delay(10_000)
        println("Finished waiting")
        consumer.stopPolling()
    }
