package it.unibo.sap.ass02.service

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val consumer = UserEventConsumer()
    consumer.use {
        it.asyncUnlimitedConsume(UserResolver::handleEvent)
    }
}