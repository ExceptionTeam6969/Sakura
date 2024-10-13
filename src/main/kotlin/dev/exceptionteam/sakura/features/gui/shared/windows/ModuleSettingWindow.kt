package dev.exceptionteam.sakura.features.gui.shared.windows

import dev.exceptionteam.sakura.events.EventBus
import dev.exceptionteam.sakura.features.gui.shared.Window
import dev.exceptionteam.sakura.features.gui.shared.component.*
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.settings.*

class ModuleSettingWindow(
    x: Float,
    y: Float,
    width: Float,
    compHeight: Float,
    module: AbstractModule
) : Window(module.name, x, y, width, compHeight) {
    init {
        module.settings.forEach {
            when (it) {
                is BooleanSetting -> BooleanComponent(width, compHeight, it)
                is NumberSetting -> SliderComponent(width, compHeight, it)
                is KeyBindSetting -> BindComponent(width, compHeight, it)
                is EnumSetting<*> -> EnumComponent(width, compHeight, it)
                is ColorSetting -> ColorComponent(width, compHeight, it)
                else -> null
            }?.also {
                addComponent(it)
            }
        }
        updatePosition(x, y)
    }

    override fun onOpen() {
        components.forEach {
            EventBus.subscribe(it)
        }
    }

    override fun onClose() {
        components.forEach {
            EventBus.unsubscribe(it)
        }
    }
}