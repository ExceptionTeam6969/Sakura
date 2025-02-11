package dev.exceptionteam.sakura.graphics.color

import java.awt.Color
import kotlin.math.ceil

object RainbowColor {
    fun astolfo(yDist: Float, yTotal: Float, saturation: Float, speedt: Float): Color {
        var hue: Float
        val speed = 1800.0f
        hue = (System.currentTimeMillis() % speed.toInt().toLong()).toFloat() + (yTotal - yDist) * speedt
        while (hue > speed) {
            hue -= speed
        }
        if (speed.let { hue /= it; hue }.toDouble() > 0.5) {
            hue = 0.5f - (hue - 0.5f)
        }
        return Color.getHSBColor(0.5f.let { hue += it; hue }, saturation, 1.0f)
    }

    fun astolfo(clickgui: Boolean, yOffset: Int): Color {
        val speed = (if (clickgui) 35 * 100 else 30 * 100).toFloat()
        var hue = (System.currentTimeMillis() % speed.toInt() + yOffset).toFloat()
        if (hue > speed) {
            hue -= speed
        }
        hue /= speed
        if (hue > 0.5f) {
            hue = 0.5f - (hue - 0.5f)
        }
        hue += 0.5f
        return Color.getHSBColor(hue, 0.4f, 1f)
    }


    fun getRainbow(delay: Int, saturation: Float, brightness: Float): Color {
        var rainbow = ceil(((System.currentTimeMillis() + delay) / 16f).toDouble())
        rainbow %= 360.0
        return Color.getHSBColor((rainbow / 360).toFloat(), saturation, brightness)
    }

    fun getRainbow(speed: Float, saturation: Float, brightness: Float): Int {
        val hue = (System.currentTimeMillis() % 11520L).toFloat() / 11520.0f * speed
        return Color.HSBtoRGB(hue, saturation, brightness)
    }

    fun getRainbowColor(speed: Float, saturation: Float, brightness: Float): Color {
        return Color(getRainbow(speed, saturation, brightness))
    }

    fun getRainbowColor(speed: Float, saturation: Float, brightness: Float, add: Long): Color {
        return Color(getRainbow(speed, saturation, brightness, add))
    }

    fun getRainbow(speed: Float, saturation: Float, brightness: Float, add: Long): Int {
        val hue = ((System.currentTimeMillis() + add) % 11520L).toFloat() / 11520.0f * speed
        return Color.HSBtoRGB(hue, saturation, brightness)
    }
}