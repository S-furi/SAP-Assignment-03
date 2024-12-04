package it.unibo.sap.ass02.dashboard.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CoroutineHelper {
    fun runGuiAsyncUpdate(block: suspend () -> Unit) = CoroutineScope(Dispatchers.IO).launch { block() }
}
