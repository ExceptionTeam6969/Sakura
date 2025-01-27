package dev.exceptionteam.sakura.features.modules.impl.client

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object Rotations: Module(
    name = "rotations",
    category = Category.CLIENT,
    alwaysEnable = true,
) {

    val packetRotation by setting("packet-rotation", false)

}