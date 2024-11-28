package it.unibo.sap.ass02.dashboard.presentation

import it.unibo.sap.ass02.domain.Ride
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities

class RideSimulationControlPanel(
    ride: Ride,
) : JFrame("Ride ${ride.id}- Control Panel") {
    private val listeners = mutableListOf<RideViewListener>()

    init {
        val stopButton =
            JButton("Stop Riding").apply {
                addActionListener { _ ->
                    listeners.forEach { it.stopRide(ride.id) }
                    dispose()
                }
            }
        val inputPanel =
            JPanel(GridLayout(3, 2, 10, 10))
                .apply {
                    add(JLabel("Rider name: ${ride.user}"))
                    add(JLabel("Riding ebike: ${ride.bike}"))
                }
        val btnPanel = JPanel().apply { add(stopButton) }

        setSize(400, 200)
        layout = BorderLayout(10, 10)
        add(inputPanel, BorderLayout.CENTER)
        add(btnPanel, BorderLayout.SOUTH)
    }

    fun display() = SwingUtilities.invokeLater { isVisible = true }

    fun addRideEventListener(listener: RideViewListener) = this.listeners.add(listener)
}
