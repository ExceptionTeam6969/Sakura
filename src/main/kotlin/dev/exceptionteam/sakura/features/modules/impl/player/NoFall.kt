package dev.exceptionteam.sakura.features.modules.impl.player

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.mixins.packet.ServerboundMovePlayerPacketAccessor
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

object NoFall: Module(
    name = "no-fall",
    category = Category.PLAYER
) {

    private val fallDistance by setting("fall-distance", 1.7f, 0.0f..3f)

    init {

        nonNullListener<PacketEvents.Send> { e ->

            if (e.packet !is ServerboundMovePlayerPacket) return@nonNullListener

            if (player.fallDistance >= fallDistance) {
                (e.packet as ServerboundMovePlayerPacketAccessor).setOnGround(true)
            }

        }

    }

}