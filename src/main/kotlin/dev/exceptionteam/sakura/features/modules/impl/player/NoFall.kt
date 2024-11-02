package dev.exceptionteam.sakura.features.modules.impl.player

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

object NoFall: Module(
    name = "no-fall",
    category = Category.PLAYER
) {

    private val fallDistance by setting("fall-distance", 1.7f, 0.0f..3f)

    init {

        nonNullListener<PacketEvents.Send> { e ->

            if (e.packet !is PlayerMoveC2SPacket) return@nonNullListener

            if (player.fallDistance >= fallDistance) {
                e.packet.onGround = true
            }

        }

    }

}