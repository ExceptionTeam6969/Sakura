package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object Test: Module(
    name = "test",
    category = Category.MOVEMENT
) {

    private val slider by setting("slider", 0.5, 0.0..1.0)

}
