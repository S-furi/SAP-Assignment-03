package it.unibo.sap.ass02.dashboard.presentation

import it.unibo.sap.ass02.dashboard.controller.ServiceProvider
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel

class RideVisualPanel(
    w: Int,
    h: Int,
) : JPanel() {
    private val dx: Long = (w / 2 - 20).toLong()
    private val dy: Long = (h / 2 - 20).toLong()

    override fun paint(g: Graphics?) {
        (g as? Graphics2D)
            ?.apply {
                setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
                clearRect(0, 0, width, height)
            }?.also { graphics2D ->
                ServiceProvider.EBikeService.getAll().forEach { bike ->
                    val p = bike.location
                    val x0 = (dx + p.x).toInt()
                    val y0 = (dy - p.y).toInt()
                    graphics2D.drawOval(x0, y0, 20, 20)
                    graphics2D.drawString(bike.id, x0, y0 + 35)
                    graphics2D.drawString("(${p.x.toInt()}, ${p.y.toInt()})", x0, y0 + 50)
                }
            }?.also { graphics2D ->
                var y = 20
                ServiceProvider.UserService.getAll().forEach { user ->
                    graphics2D.drawRect(10, y, 20, 20)
                    graphics2D.drawString("${user.id} - balance: ${user.credit}", 35, y + 15)
                    y += 25
                }
            }
    }
}
