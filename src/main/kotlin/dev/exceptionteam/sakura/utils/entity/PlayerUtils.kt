package dev.exceptionteam.sakura.utils.entity

import dev.exceptionteam.sakura.events.NonNullContext

object PlayerUtils {

    fun NonNullContext.isMoving() =
        player.sidewaysSpeed != 0f || player.upwardSpeed != 0f || player.forwardSpeed != 0f

}