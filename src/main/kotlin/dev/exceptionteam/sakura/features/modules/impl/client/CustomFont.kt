package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object CustomFont: Module(
    name = "custom-font",
    category = Category.CLIENT,
    defaultEnable = true
) {

    val fontSize by setting("font-size", 12, 1..40)

}