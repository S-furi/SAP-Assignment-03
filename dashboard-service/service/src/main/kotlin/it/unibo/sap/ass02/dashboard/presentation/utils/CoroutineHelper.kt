package it.unibo.sap.ass02.dashboard.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.swing.SwingUtilities

object CoroutineHelper {
    fun runGuiAsyncUpdate(block: suspend () -> Unit) =
        SwingUtilities.invokeLater {
            CoroutineScope(Dispatchers.Default).launch { block() }
        }
}
