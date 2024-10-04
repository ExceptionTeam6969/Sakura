package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object CustomFont: Module(
    name = "CustomFont",
    category = Category.CLIENT,
    description = "Custom font",
    defaultEnable = true
) {

    val fontSize by setting("Font Size", 12, 1..30)

}