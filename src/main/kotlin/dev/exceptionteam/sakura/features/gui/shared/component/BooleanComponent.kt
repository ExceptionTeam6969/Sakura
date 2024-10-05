package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.settings.BooleanSetting
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers

class BooleanComponent(
    width: Float,
    height: Float,
    private val setting: BooleanSetting
) : AbstractComponent(0f, 0f, width, height) {
    override fun render() {
        if (setting.value) {
            RenderUtils2D.drawRectFilled(x, y, width, height, ColorRGB.GREEN)
        }

        FontRenderers.default.drawString(setting.key.translation, x + 5f, y + 2f, ColorRGB.BLACK)
    }
}