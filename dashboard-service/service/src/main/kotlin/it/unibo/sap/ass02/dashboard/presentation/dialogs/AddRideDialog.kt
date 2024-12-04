package it.unibo.sap.ass02.dashboard.presentation.dialogs

import it.unibo.sap.ass02.dashboard.controller.AsyncObserver
import it.unibo.sap.ass02.dashboard.controller.RideCommand
import it.unibo.sap.ass02.dashboard.controller.ServiceProvider
import it.unibo.sap.ass02.dashboard.presentation.ObservableView
import it.unibo.sap.ass02.dashboard.presentation.RideSimulationControlPanel
import it.unibo.sap.ass02.dashboard.presentation.RideViewListener
import it.unibo.sap.ass02.dashboard.presentation.utils.CoroutineHelper
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class AddRideDialog(
    parentFrame: JFrame,
) : JDialog(parentFrame, "Start Ride", true),
    ObservableView<RideCommand> {
    private val observers = mutableListOf<AsyncObserver<RideCommand>>()

    private val inputPanel = JPanel(GridLayout(3, 2, 10, 10))
    private val buttonPanel = JPanel()

    private val userIdField =
        JTextField(15).also {
            inputPanel.add(JLabel("UserId"))
            inputPanel.add(it)
        }

    private val ebikeIdField =
        JTextField(15).also {
            inputPanel.add(JLabel("EBikeId"))
            inputPanel.add(it)
        }

    init {
        layout = BorderLayout(10, 10)

        JButton("OK").apply {
            buttonPanel.add(this)
            addActionListener { _ ->
                CoroutineHelper.runGuiAsyncUpdate {
                    val userId = userIdField.text.toInt()
                    val ebikeId = ebikeIdField.text
                    observers.forEach { it.notifiedUpdateRequested(RideCommand.start(userId, ebikeId)) }
                    ServiceProvider.RideService.getRideFromUserIdAndBikeId(userId, ebikeId)?.let {
                        RideSimulationControlPanel(it).apply {
                            observers.filterIsInstance<RideViewListener>().forEach(this::addRideEventListener)
                            display()
                        }
                    }
                    observers.forEach { it.notifiedUpdateRequested(RideCommand.subscribe(userId, ebikeId)) }
                }
                dispose()
            }
        }

        JButton("Cancel").apply {
            buttonPanel.add(this)
            addActionListener { dispose() }
        }

        add(inputPanel, BorderLayout.CENTER)
        add(buttonPanel, BorderLayout.SOUTH)
        pack()
        setLocationRelativeTo(parentFrame)
    }

    fun display() {
        isVisible = true
    }

    override fun addObserver(obs: AsyncObserver<RideCommand>) {
        this.observers.add(obs)
    }
}
