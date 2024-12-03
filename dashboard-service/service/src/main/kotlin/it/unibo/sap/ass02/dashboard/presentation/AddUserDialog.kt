package it.unibo.sap.ass02.dashboard.presentation

import it.unibo.sap.ass02.dashboard.controller.AsyncObserver
import it.unibo.sap.ass02.dashboard.presentation.utils.CoroutineHelper.runGuiAsyncUpdate
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class AddUserDialog(
    parentFrame: JFrame,
) : JDialog(parentFrame, "Add User", true),
    ObservableView<Int> {
    private val observers = mutableListOf<AsyncObserver<Int>>()

    private val idField = JTextField(15)

    private val okButton =
        JButton("OK").apply {
            addActionListener { _ ->
                runGuiAsyncUpdate {
                    observers.forEach { it.notifiedUpdateRequested(idField.text.toInt()) }
                    dispose()
                }
            }
        }

    private val cancelButton =
        JButton("Cancel").apply {
            addActionListener { _ -> dispose() }
        }

    init {
        val inputPanel = JPanel(GridLayout(3, 2, 10, 10))
        inputPanel.add(JLabel("User ID: "))
        inputPanel.add(idField)

        val buttonPanel =
            JPanel().apply {
                add(okButton)
                add(cancelButton)
            }
        layout = BorderLayout(10, 10)
        add(inputPanel, BorderLayout.CENTER)
        add(buttonPanel, BorderLayout.SOUTH)
        pack()
        setLocationRelativeTo(parentFrame)
    }

    override fun addObserver(obs: AsyncObserver<Int>) {
        this.observers.add(obs)
    }

    fun display() {
        isVisible = true
    }
}
