package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.modules.impl.client.ClickGUI
import dev.exceptionteam.sakura.features.settings.EnumSetting
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.managers.impl.TranslationManager
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum

class EnumComponent(
    width: Float, height: Float, private val setting: EnumSetting<*>
): AbstractComponent(0f, 0f, width, height) {

    override val visible: Boolean
        get() = setting.visibility.invoke()

    override fun render() {
        val value =
            if (setting.value is TranslationEnum)
                TranslationManager.getTranslation("${setting.key.fullKey}.${(setting.value as TranslationEnum).key}")
            else setting.value.name

        FontRenderers.drawString(
            "${setting.key.translation}: " + value,
            x + 5f, y + 4f, ClickGUI.textColor)
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        if (type == MouseButtonType.LEFT) {
            setting.forwardLoop()
            return true
        }
        return false
    }

}