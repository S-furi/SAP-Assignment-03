package it.unibo.sap.ass02.infrastructure

import io.ktor.client.request.get
import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.infrastructure.utils.JsonUtils
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import java.util.concurrent.Future

class UserAPI : AbstractAPI<User, Int, Int>() {
    private val kafkaHost: String = System.getenv("KAFKA_HOST") ?: "localhost"
    private val kafkaPort: Int = System.getenv("KAFKA_PORT")?.toInt() ?: 29092
    private val topicName = System.getenv("UPDATE_USER_CREDIT_TOPIC_NAME") ?: "update-credit"

    private val userEventProducer =
        KafkaProducer<Int, Int>(
            mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "$kafkaHost:$kafkaPort",
                ProducerConfig.ACKS_CONFIG to "all",
                ProducerConfig.RETRIES_CONFIG to 0,
                ProducerConfig.BATCH_SIZE_CONFIG to 16384,
                ProducerConfig.LINGER_MS_CONFIG to 1,
                ProducerConfig.BUFFER_MEMORY_CONFIG to 33554432,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.IntegerSerializer",
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.IntegerSerializer",
            ),
        )

    override suspend fun add(config: Int): Boolean =
        userEventProducer.send(topicName, config, 100).also { userEventProducer.flush() }.get() != null

    override suspend fun getAll(): List<User> =
        client.get(ServicesRoutes.USERS_ROUTE + "/all").let {
            JsonUtils.decodeHttpPayload<List<User>>(it)
                ?: listOf()
        }

    override suspend fun get(id: Int): User? =
        client.get("${ServicesRoutes.USERS_ROUTE}/$id").let {
            JsonUtils.decodeHttpPayload<User>(it)
        }

    fun <K, V> KafkaProducer<K, V>.send(
        topicName: String,
        key: K,
        value: V,
    ): Future<RecordMetadata> {
        val record = ProducerRecord(topicName, 0, System.currentTimeMillis(), key, value, listOf())
        return send(record)
    }
}
