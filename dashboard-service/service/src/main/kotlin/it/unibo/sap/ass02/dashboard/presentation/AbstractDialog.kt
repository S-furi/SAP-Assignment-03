package it.unibo.sap.ass02.dashboard.presentation

import javax.swing.JDialog
import javax.swing.JFrame

abstract class AbstractDialog<D>(
    parentFrame: JFrame,
    title: String,
) : JDialog(parentFrame, title, true) {

    abstract fun initializeComponents(): Unit
    abstract fun setUpLayout(): Unit
    abstract fun addEventHandlers(): Unit
}
