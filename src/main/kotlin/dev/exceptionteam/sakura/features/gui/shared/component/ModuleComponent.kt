package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.gui.clickgui.ClickGUIScreen
import dev.exceptionteam.sakura.features.gui.hudeditor.HUDEditorScreen
import dev.exceptionteam.sakura.features.gui.shared.windows.ModuleSettingWindow
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.modules.HUDModule
import dev.exceptionteam.sakura.features.modules.impl.client.UiSetting
import dev.exceptionteam.sakura.graphics.easing.AnimationFlag
import dev.exceptionteam.sakura.graphics.easing.Easing
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType

class ModuleComponent(
    private val module: AbstractModule,
    x: Float, y: Float, width: Float, height: Float
) : AbstractComponent(x, y, width, height) {

    private val widthAnimationFlag = AnimationFlag(Easing.LINEAR, 300f)

    override fun render() {
        val widthPercent = widthAnimationFlag.getAndUpdate(if (module.isEnabled) 1f else 0f)

        if (widthPercent > 0.01f) RenderUtils2D.drawRectFilled(
            x + 2f, y + 1f, width * widthPercent - 4f, height - 2f,
            UiSetting.secondaryColor.alpha(widthPercent.coerceIn(0f, UiSetting.secondaryColor.aFloat)))

        FontRenderers.drawString(
            module.name.translation,
            x + 5f, y + 4f,
            UiSetting.textColor
        )
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        if (!checkHovering()) {
            return false
        }

        when (type) {
            MouseButtonType.LEFT -> {
                if (!module.alwaysEnable) module.toggle()
                else if (module.isDisabled) module.enable()
            }

            MouseButtonType.RIGHT -> {
                val moduleSettingWindow = ModuleSettingWindow(mouseX, mouseY, width, height, module)


                if (module is HUDModule) HUDEditorScreen.currentWindow = moduleSettingWindow
                else ClickGUIScreen.currentWindow = moduleSettingWindow
            }

            else -> {
                return false
            }
        }

        return true
    }
}