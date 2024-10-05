package dev.exceptionteam.sakura.features.gui.shared.windows

import dev.exceptionteam.sakura.features.gui.shared.Window
import dev.exceptionteam.sakura.features.gui.shared.component.BooleanComponent
import dev.exceptionteam.sakura.features.modules.AbstractModule
import dev.exceptionteam.sakura.features.settings.BooleanSetting
import dev.exceptionteam.sakura.utils.control.MouseButtonType

class ModuleSettingWindow(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    module: AbstractModule
) : Window(x, y, width,
    height
) {
    init {
        module.settings.forEach {
            when (it) {
                is BooleanSetting -> {
                    addComponent(BooleanComponent(width, height, it))
                }

                else -> {}
            }
        }
        updatePosition(x, y)
    }
}