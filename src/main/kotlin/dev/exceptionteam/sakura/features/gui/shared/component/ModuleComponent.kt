package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen
import dev.exceptionteam.sakura.features.gui.shared.windows.ModuleSettingWindow
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType

class ModuleComponent(
    private val module: AbstractModule,
    x: Float, y: Float, width: Float, height: Float
) : AbstractComponent(x, y, width, height) {
    override fun render() {
        RenderUtils2D.drawRectFilled(x, y, width, height, ColorRGB.WHITE)

        FontRenderers.drawString(
            module.name.translation,
            x + 5f, y + 4f,
            ColorRGB.BLACK
        )
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        if (!checkHovering()) {
            return false
        }

        when (type) {
            MouseButtonType.LEFT -> {
                module.toggle()
            }

            MouseButtonType.RIGHT -> {
                val moduleSettingWindow = ModuleSettingWindow(mouseX, mouseY, width, height, module)
                ClickGUIScreen.currentWindow = moduleSettingWindow
            }

            else -> {
                return false
            }
        }

        return true
    }
}