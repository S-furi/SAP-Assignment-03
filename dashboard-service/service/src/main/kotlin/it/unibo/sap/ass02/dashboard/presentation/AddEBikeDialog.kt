package it.unibo.sap.ass02.dashboard.presentation

import it.unibo.sap.ass02.dashboard.controller.AsyncObserver
import it.unibo.sap.ass02.dashboard.controller.ServiceProvider
import it.unibo.sap.ass02.dashboard.presentation.utils.CoroutineHelper.runGuiAsyncUpdate
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingUtilities

class AddEBikeDialog(
    parentFrame: JFrame,
) : JDialog(parentFrame, "Add Ebike", true),
    ObservableView<Map<String, Any>> {
    private val observers = mutableListOf<AsyncObserver<Map<String, Any>>>()

    private val inputPanel = JPanel(GridLayout(3, 2, 10, 10))
    private val buttonPanel = JPanel()

    private val idField = JTextField(15).also { addFieldWithLabel(it, "EBike ID: ") }
    private val xCoordField = JTextField(15).also { addFieldWithLabel(it, "X coord: ") }
    private val yCoordField = JTextField(15).also { addFieldWithLabel(it, "Y coord: ") }

    private val okButton =
        JButton("OK").apply {
            buttonPanel.add(this)
            addActionListener { _ ->
                val data =
                    mapOf(
                        "id" to idField.text,
                        "x" to xCoordField.text.toDouble(),
                        "y" to yCoordField.text.toDouble(),
                    )
                runGuiAsyncUpdate {
                    observers.forEach { it.notifiedUpdateRequested(data) }
                }
                dispose()
            }
        }
    private val cancelButton =
        JButton("Cancel").apply {
            buttonPanel.add(this)
            addActionListener { dispose() }
        }

    init {
        layout = BorderLayout(10, 10)
        add(inputPanel, BorderLayout.CENTER)
        add(buttonPanel, BorderLayout.SOUTH)
        pack()
    }

    private fun addFieldWithLabel(
        field: JTextField,
        label: String,
    ) = inputPanel.apply {
        add(JLabel(label))
        add(field)
    }

    override fun addObserver(obs: AsyncObserver<Map<String, Any>>) {
        this.observers.add(obs)
    }
}

fun main() {
    SwingUtilities.invokeLater {
        AddEBikeDialog(JFrame()).apply {
            addObserver(ServiceProvider.EBikeService)
            isVisible = true
        }
    }
}
