package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object CombatSettings: Module(
    name = "combat-settings",
    category = Category.CLIENT,
    alwaysEnable = true,
) {

    val assumeResistance by setting("assume-resistance", true)
    val backSideSampling by setting("back-side-sampling", true)
    val horizontalCenterSampling by setting("horizontal-center-sampling", false)
    val verticalCenterSampling by setting("vertical-center-sampling", true)

}