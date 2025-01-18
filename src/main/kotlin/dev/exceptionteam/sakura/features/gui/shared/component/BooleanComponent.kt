package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.modules.impl.client.UiSetting
import dev.exceptionteam.sakura.features.settings.BooleanSetting
import dev.exceptionteam.sakura.graphics.RenderUtils2D
import dev.exceptionteam.sakura.graphics.easing.AnimationFlag
import dev.exceptionteam.sakura.graphics.easing.Easing
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType

class BooleanComponent(
    width: Float, height: Float, private val setting: BooleanSetting
) : AbstractComponent(0f, 0f, width, height) {
    override val visible: Boolean
        get() = setting.visibility.invoke()
    
    private val widthAnimationFlag = AnimationFlag(Easing.LINEAR, 300f)

    override fun render() {
        val widthPercent = widthAnimationFlag.getAndUpdate(if (setting.value) 1f else 0f)

        if (widthPercent > 0.01f) RenderUtils2D.drawRectFilled(
            x + 2f, y + 1f, width * widthPercent - 4f, height - 2f,
            UiSetting.secondaryColor.alpha(widthPercent.coerceIn(0f, UiSetting.secondaryColor.aFloat)))
        FontRenderers.drawString(setting.key, x + 5f, y + 4f, UiSetting.textColor, UiSetting.shadow)
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        if (type == MouseButtonType.LEFT) {
            setting.value = !setting.value
            return true
        }
        return false
    }
}