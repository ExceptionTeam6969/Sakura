package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object RenderSystemMod: Module(
    name = "render-system",
    category = Category.CLIENT,
    description = "Custom render system"
) {

    val frameBuffer by setting("frame-buffer", true)

}