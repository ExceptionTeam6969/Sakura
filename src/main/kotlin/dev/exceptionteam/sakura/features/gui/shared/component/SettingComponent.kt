package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.settings.AbstractSetting

class SettingComponent<T>(
    val setting: AbstractSetting<T>,
    width: Float,
    height: Float
) : AbstractComponent(0f, 0f, width, height)