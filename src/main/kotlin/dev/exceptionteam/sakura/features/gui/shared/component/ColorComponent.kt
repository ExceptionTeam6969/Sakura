package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.settings.ColorSetting
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType

class ColorComponent(
    width: Float, height: Float, private val setting: ColorSetting
): AbstractComponent(0f, 0f, width, height) {

    private val closedHeight = height
    private val openHeight = height + 80f

    private var isOpen = false

    override val visible: Boolean
        get() = setting.visibility.invoke()

    override fun render() {
        FontRenderers.drawString(setting.key, x + 5f, y + 4f, ColorRGB.BLACK)
        RenderUtils2D.drawRectFilled(x + width - 25f, y + 3f, 20f, closedHeight - 6f, setting.value)

        if (!isOpen) return

        renderPicker()
    }

    fun renderPicker() {
        // TODO: render picker
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