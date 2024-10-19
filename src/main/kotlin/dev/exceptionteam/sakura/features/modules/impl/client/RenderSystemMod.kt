package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object RenderSystemMod: Module(
    name = "render-system",
    category = Category.CLIENT,
    defaultEnable = true,
    alwaysEnable = true,
) {

    val frameBuffer by setting("frame-buffer", true)

}