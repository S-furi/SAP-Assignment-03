package it.unibo.sap.ass02.dashboard.presentation

import it.unibo.sap.ass02.domain.EBike
import it.unibo.sap.ass02.domain.User
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.util.concurrent.ConcurrentHashMap
import javax.swing.JPanel

class RideVisualPanel(
    w: Int,
    h: Int,
) : JPanel() {
    private val dx: Long = (w / 2 - 20).toLong()
    private val dy: Long = (h / 2 - 20).toLong()

    private var ebikes = ConcurrentHashMap<String, EBike>()
    private var users = ConcurrentHashMap<Int, User>()

    init {
        refresh()
    }

    override fun paint(g: Graphics?) {
        (g as? Graphics2D)
            ?.apply {
                setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
                clearRect(0, 0, width, height)
            }?.also { graphics2D ->
                ebikes.forEach { (_, bike) ->
                    val p = bike.location
                    val x0 = (dx + p.x).toInt()
                    val y0 = (dy - p.y).toInt()
                    graphics2D.drawOval(x0, y0, 20, 20)
                    graphics2D.drawString(bike.id, x0, y0 + 35)
                    graphics2D.drawString("(${p.x.toInt()}, ${p.y.toInt()}) - (${bike.battery}%)", x0, y0 + 50)
                }
            }?.also { graphics2D ->
                var y = 20
                users.forEach { (_, user) ->
                    graphics2D.drawRect(10, y, 20, 20)
                    graphics2D.drawString("${user.id} - balance: ${user.credit}", 35, y + 15)
                    y += 25
                }
            }
    }

    fun refresh() {
        repaint()
    }

    fun notifiedUserAdded(user: User) {
        this.users[user.id] = user
        this.refresh()
    }

    fun notifiedEBikeAdded(eBike: EBike) {
        this.ebikes[eBike.id] = eBike
        this.refresh()
    }

    fun notifiedRideUpdated(
        user: User,
        ebike: EBike,
    ) {
        this.users[user.id] = user
        this.ebikes[ebike.id] = ebike
    }
}
