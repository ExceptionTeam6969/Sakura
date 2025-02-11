package dev.exceptionteam.sakura.features.modules.impl.player

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module

object CancelUsing: Module(
    name = "cancel-using",
    category = Category.PLAYER,
) {

    val shield by setting("shield", false)

}