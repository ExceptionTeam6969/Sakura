package dev.exceptionteam.sakura.features.gui.shared.windows

import dev.exceptionteam.sakura.features.gui.shared.Window
import dev.exceptionteam.sakura.features.gui.shared.component.BooleanComponent
import dev.exceptionteam.sakura.features.gui.shared.component.SliderComponent
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.settings.BooleanSetting
import dev.exceptionteam.sakura.features.settings.NumberSetting

class ModuleSettingWindow(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    module: AbstractModule
) : Window(module.name, x, y, width, height) {
    init {
        module.settings.forEach {
            when (it) {
                is BooleanSetting -> BooleanComponent(width, height, it)
                is NumberSetting -> SliderComponent(width, height, it)
                else -> null
            }?.also {
                addComponent(it)
            }
        }
        updatePosition(x, y)
    }
}