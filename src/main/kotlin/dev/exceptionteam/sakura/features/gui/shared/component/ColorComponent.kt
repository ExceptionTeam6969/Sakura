package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.settings.ColorSetting
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.color.ColorUtils
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import dev.exceptionteam.sakura.utils.math.MathUtils
import java.awt.Color

class ColorComponent(
    width: Float, height: Float, private val setting: ColorSetting
): AbstractComponent(0f, 0f, width, height) {

    private data class Rect(val x: Float, val y: Float, val width: Float, val height: Float) {
        val x2 = x + width
        val y2 = y + height
    }

    private val closedHeight = height
    private val openHeight = height + 80f

    private var isOpen = false

    override val visible: Boolean
        get() = setting.visibility.invoke()

    private var focusedPart = 0

    private var first = true

    // Saturation Brightness
    private var partSB    = Rect(8f, closedHeight + 5f, width - 44f, 70f)
    private var partHUE   = Rect(partSB.x2 + 4f, closedHeight + 5f, 10f, 70f)
    private var partAlpha = Rect(partHUE.x2 + 4f, closedHeight + 5f, 10f, 70f)

    private var hue = 0f
    private var saturation = 0f
    private var brightness = 0f
    private var alpha = 0f

    override fun render() {
        if (first) {
            val hsb = Color.RGBtoHSB(setting.value.r, setting.value.g, setting.value.b, null)
            hue = hsb[0]
            saturation = hsb[1]
            brightness = hsb[2]
            alpha = setting.value.aFloat
            first = false
        }
        FontRenderers.drawString(setting.key, x + 5f, y + 4f, ColorRGB.BLACK)
        RenderUtils2D.drawRectFilled(x + width - 25f, y + 3f, 20f, closedHeight - 6f, setting.value)

        if (!isOpen) return

        updateColor()
        renderPicker()
    }

    fun renderPicker() {
        // Saturation Brightness Part
        RenderUtils2D.drawRectGradientH(x + partSB.x, y + partSB.y, partSB.width, partSB.height, ColorUtils.hsbToRGB(hue, 0f, 1f), ColorUtils.hsbToRGB(hue, 1f, 1f))
        RenderUtils2D.drawRectGradientV(x + partSB.x, y + partSB.y, partSB.width, partSB.height, ColorRGB.EMPTY, ColorRGB.BLACK)

        //TODO: Render Circle
        RenderUtils2D.drawRectFilled(
            x + partSB.x + partSB.width * saturation - 1.5f,
            y + partSB.y + partSB.height * (1f - brightness) - 1.5f,
            3f,
            3f,
            ColorRGB.WHITE
        )

        // Hue Part
        var level = 1f
        val hues = 1f / partHUE.height
        val huex = x + partHUE.x
        val huey = y + partHUE.y
        while (level < partHUE.height) {
            val y = huey + level
            RenderUtils2D.drawLine(
                huex, y,
                huex + partHUE.width, y,
                ColorUtils.hsbToRGB(hues * level, 1f, 1f)
            )
            level++
        }

        RenderUtils2D.drawLineH(x + partHUE.x - 1f, y + partHUE.y - 1.5f + (partHUE.height * hue), partHUE.width + 2f, ColorRGB.WHITE)

        // Alpha Part
        RenderUtils2D.drawRectFilled(x + partAlpha.x, y + partAlpha.y, partAlpha.width, partAlpha.height, ColorRGB.WHITE)
        RenderUtils2D.drawRectGradientV(x + partAlpha.x, y + partAlpha.y, partAlpha.width, partAlpha.height, setting.value.alpha(255), ColorRGB.EMPTY)

        RenderUtils2D.drawLineH(x + partAlpha.x - 1f, y + partAlpha.y - 1.5f + (partAlpha.height * (1f - alpha)), partAlpha.width + 2f, ColorRGB.WHITE)
    }

    fun updateColor() {
        if (focusedPart == 0) return
        when (focusedPart) {
            1 -> {
                saturation = (mouseX - x - partSB.x).coerceIn(0f, partSB.width) / partSB.width
                brightness = (partSB.height - (mouseY - y - partSB.y).coerceIn(0f, partAlpha.height)) / partSB.height
                setting.value = ColorUtils.hsbToRGB(hue, saturation, brightness).alpha(setting.value.a)
            }
            2 -> {
                hue = 1f - (partHUE.height - (mouseY - y - partHUE.y).coerceIn(0f, partHUE.height)) / partHUE.height
                setting.value = ColorUtils.hsbToRGB(hue, saturation, brightness).alpha(setting.value.a)
            }
            3 -> {
                alpha = (partAlpha.height - (mouseY - y - partAlpha.y).coerceIn(0f, partAlpha.height)) / partAlpha.height
                setting.value = setting.value.alpha((255 * alpha).toInt())
            }
        }
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        when (type) {
            MouseButtonType.LEFT -> {
                if (!isOpen) return true
                focusedPart = when {
                    isHover(partSB) -> 1
                    isHover(partHUE) -> 2
                    isHover(partAlpha) -> 3
                    else -> 0
                }
            }

            MouseButtonType.RIGHT -> {
                isOpen = !isOpen
                height = if (isOpen) openHeight else closedHeight
                return true
            }

            else -> {}
        }
        return false
    }

    override fun mouseReleased(type: MouseButtonType): Boolean {
        return if (focusedPart != 0) {
            focusedPart = 0
            true
        } else false
    }

    private fun isHover(rect: Rect): Boolean {
        val x = x + rect.x
        val y = y + rect.y
        return mouseX >= x && mouseY >= y && mouseX <= x + rect.width && mouseY <= y + rect.height
    }
}