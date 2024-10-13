package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.settings.ColorSetting
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.color.ColorUtils
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType

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

    // Saturation Brightness
    private var partSB    = Rect(8f, closedHeight + 5f, width - 44f, 70f)
    private var partHUE   = Rect(partSB.x2 + 4f, closedHeight + 5f, 10f, 70f)
    private var partAlpha = Rect(partHUE.x2 + 4f, closedHeight + 5f, 10f, 70f)

    private var hue = 0f
    private var saturation = 0f
    private var brightness = 0f

    override fun render() {
        FontRenderers.drawString(setting.key, x + 5f, y + 4f, ColorRGB.BLACK)
        RenderUtils2D.drawRectFilled(x + width - 25f, y + 3f, 20f, closedHeight - 6f, setting.value)

        if (!isOpen) return

        renderPicker()
    }

    fun renderPicker() {
        // Saturation Brightness Part
        RenderUtils2D.drawRectGradientH(x + partSB.x, y + partSB.y, partSB.width, partSB.height, ColorUtils.hsbToRGB(hue, 0f, 1f), ColorUtils.hsbToRGB(hue, 1f, 1f))
        RenderUtils2D.drawRectGradientV(x + partSB.x, y + partSB.y, partSB.width, partSB.height, ColorRGB.EMPTY, ColorRGB.BLACK)

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

        // Alpha Part
        RenderUtils2D.drawRectFilled(x + partAlpha.x, y + partAlpha.y, partAlpha.width, partAlpha.height, ColorRGB.WHITE)
        RenderUtils2D.drawRectGradientV(x + partAlpha.x, y + partAlpha.y, partAlpha.width, partAlpha.height, setting.value.alpha(255), ColorRGB.EMPTY)
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        when (type) {
            MouseButtonType.LEFT -> {
                if (!isOpen) return true
                // TODO: set value
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

}