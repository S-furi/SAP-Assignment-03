package it.unibo.sap.ass02.dashboard.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

object CoroutineHelper {
    fun runGuiAsyncUpdate(block: suspend () -> Unit) = CoroutineScope(Dispatchers.Default).launch { block() }

    object Extension {
        private val logger = LoggerFactory.getLogger(Extension::class.java)

        fun Job.onCompletion(block: () -> Unit) =
            this.invokeOnCompletion {
                it?.let {
                    logger.error("Something went wrong, got: ${it.message}.\n- Current stacktrace: \n${it.stackTraceToString()}")
                    return@invokeOnCompletion
                }
                block()
            }
    }
}
