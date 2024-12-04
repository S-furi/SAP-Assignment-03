package it.unibo.sap.ass02.dashboard.controller

interface AsyncObserver<C> {
    suspend fun notifiedUpdateRequested(update: C)
}
