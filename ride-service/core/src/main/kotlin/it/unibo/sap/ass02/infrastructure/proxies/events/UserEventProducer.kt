package it.unibo.sap.ass02.infrastructure.proxies.events

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import java.time.Duration

class UserEventProducer : EventProducer() {
    private val topicName = System.getenv("UPDATE_USER_CREDIT_TOPIC_NAME") ?: "update-credit"
    private val producer = KafkaProducer<Int, Int>(
        defaultKafkaParameters +
            mapOf(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.IntegerSerializer",
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.IntegerSerializer",
            ),
        )

    fun increaseUserCredit(userId: Int, amount: Int) = producer.send(topicName, userId, amount).also { producer.flush() }

    fun decreaseUserCredit(userId: Int, amount: Int) = producer.send(topicName, userId, -amount).also { producer.flush() }

    override fun close() {
        runCatching { producer.close(Duration.ofMillis(5_000)) }
    }
}
