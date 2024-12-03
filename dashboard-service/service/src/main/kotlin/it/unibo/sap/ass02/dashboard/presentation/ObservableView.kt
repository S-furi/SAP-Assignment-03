package it.unibo.sap.ass02.dashboard.presentation

import it.unibo.sap.ass02.dashboard.controller.AsyncObserver

interface ObservableView<C> {
    fun addObserver(obs: AsyncObserver<C>)
}
