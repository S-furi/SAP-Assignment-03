package it.unibo.sap.ass02.dashboard

import it.unibo.sap.ass02.dashboard.controller.ServiceProvider
import kotlinx.coroutines.runBlocking

fun main() =
    runBlocking {
        ServiceProvider.display()
    }
