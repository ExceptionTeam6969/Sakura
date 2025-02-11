package dev.exceptionteam.sakura.features.modules.impl.render

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object FullBright: Module(
    name = "full-bright",
    category = Category.RENDER
) {

    val brightness by setting("brightness", 12, 0..12)

}