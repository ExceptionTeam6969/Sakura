package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.events.impl.KeyEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.gui.shared.component.ModuleComponent.Companion.positionAnimationFlag
import dev.exceptionteam.sakura.features.modules.impl.client.UiSetting
import dev.exceptionteam.sakura.features.settings.KeyBindSetting
import dev.exceptionteam.sakura.graphics.color.ColorRGB
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.KeyBind
import dev.exceptionteam.sakura.utils.control.MouseButtonType
import org.lwjgl.glfw.GLFW

class BindComponent(
    width: Float, height: Float, private val setting: KeyBindSetting
): AbstractComponent(0f, 0f, width, height) {

    init {
        nonNullListener<KeyEvent>(priority = Int.MAX_VALUE) {
            if (!listening) return@nonNullListener
            if (it.key == GLFW.GLFW_KEY_DELETE) setting.value = KeyBind()
            else setting.value = it.keyBind
            listening = false
            it.cancel()
        }
    }

    private var listening = false

    override val visible: Boolean
        get() = setting.visibility.invoke()

    override fun render() {
        val newColor = ColorRGB(
            UiSetting.textColor.r,
            UiSetting.textColor.g,
            UiSetting.textColor.b,
            (UiSetting.textColor.a * positionAnimationFlag.get()).toInt()
        )
        FontRenderers.drawString(
            "${setting.key.translation}: " + if (listening) "..." else setting.value.keyName,
            x + 5f, y + 4f, newColor, UiSetting.shadow)
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        if (type == MouseButtonType.LEFT) {
            listening = !listening
            return true
        }
        return false
    }

}