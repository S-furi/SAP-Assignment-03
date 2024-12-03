package it.unibo.sap.ass02.dashboard.controller

import it.unibo.sap.ass02.domain.EBike
import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.infrastructure.AbstractAPI
import it.unibo.sap.ass02.infrastructure.EBikeAPI
import it.unibo.sap.ass02.infrastructure.UserAPI
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object ServiceProvider {
    object EBikeService : AsyncObserver<Map<String, Any>>, Service<EBike, Map<String, Any>, String>(EBikeAPI()) {
        override suspend fun notifiedUpdateRequested(update: Map<String, Any>) {
            updateRequested(update, apis::add)
        }
    }

    object UserService : AsyncObserver<Int>, Service<User, Int, Int>(UserAPI()) {
        override suspend fun notifiedUpdateRequested(update: Int) {
            updateRequested(update, apis::add)
        }
    }

    abstract class Service<T, C, I>(
        val apis: AbstractAPI<T, C, I>,
    ) {
        // TODO: do a non blocking call
        fun getAll(): List<T> = runBlocking { apis.getAll() }

        suspend fun updateRequested(
            update: C,
            func: suspend (C) -> Unit,
        ) = coroutineScope {
            launch {
                func(update)
            }
        }
    }
}
