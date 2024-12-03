package it.unibo.sap.ass02.dashboard.presentation

import it.unibo.sap.ass02.domain.EBike
import it.unibo.sap.ass02.domain.Ride
import it.unibo.sap.ass02.domain.User
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel

class EBikeAppView :
    JFrame("EBike APP"),
    ModelListener,
    ActionListener {
    private val width = 800
    private val height = 500

    private val listeners = mutableListOf<RideViewListener>()

    private val centralPanel = RideVisualPanel(width, height).also { this.add(it, BorderLayout.CENTER) }
    private val topPanel = JPanel()
    private val addUserButton: JButton = JButton("Add User").also(::configureButton)
    private val addEBikeButton: JButton = JButton("Add EBike").also(::configureButton)
    private val startRideButton: JButton = JButton("Start Ride").also(::configureButton)

    init {
        setSize(width, height)
        isResizable = false
        layout = BorderLayout()
        defaultCloseOperation = EXIT_ON_CLOSE
        defaultCloseOperation = EXIT_ON_CLOSE
        add(topPanel, BorderLayout.NORTH)
    }

    override fun actionPerformed(e: ActionEvent): Unit =
        when (e.source) {
            (e.source == addUserButton) -> AddUserDialog(this).isVisible = true
            (e.source == addEBikeButton) -> AddEBikeDialog(this).isVisible = true
            (e.source == startRideButton) -> TODO("Not yet implemented")
            else -> JDialog(this, "Command not recognized").isVisible = true
        }

    private fun configureButton(btn: JButton) = btn.addActionListener(this).apply { topPanel.add(btn) }

    fun addRideEventListener(listener: RideViewListener) = this.listeners.add(listener)

    override fun notifiedRideUpdate(ride: Ride) {
        this.centralPanel.refresh()
    }

    override fun notifiedUserAdded(user: User) {
        TODO("Not yet implemented")
    }

    override fun notifiedEBikeAdded(eBike: EBike) {
        TODO("Not yet implemented")
    }
}
