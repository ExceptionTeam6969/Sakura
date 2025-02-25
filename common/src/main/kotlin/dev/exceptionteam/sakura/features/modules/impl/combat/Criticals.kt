package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.mixins.core.Interface.ILocalPlayer
import io.netty.buffer.Unpooled
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.game.ServerboundInteractPacket
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

object Criticals: Module(
    name = "criticals",
    category = Category.COMBAT
) {

    private val grim by setting("grim", false)

    init {
        nonNullListener<PacketEvents.Send> { event ->
            if (event.packet is ServerboundInteractPacket) {
                val playerInteractPacket = event.packet
                val packetBuf = FriendlyByteBuf(Unpooled.buffer())
                playerInteractPacket.write(packetBuf)
                packetBuf.readVarInt()
                val type = packetBuf.readEnum(ServerboundInteractPacket.ActionType::class.java)
                if (type == ServerboundInteractPacket.ActionType.ATTACK && player.onGround()) {
                    if (grim) {
                        crit(-0.000001,true)
                    } else {
                        crit(0.000000271875, false)
                        crit(0.0, false)
                    }
                }
            }
        }
    }

    private fun NonNullContext.crit(increase: Double, full: Boolean) {
        if (full) {
            connection.send(ServerboundMovePlayerPacket.PosRot(
                player.x,
                player.y + increase,
                player.z,
                (player as ILocalPlayer).lastPitch,
                (player as ILocalPlayer).lastYaw,
                false,
                player.horizontalCollision
            ))
        } else {
            connection.send(ServerboundMovePlayerPacket.Pos(
                player.x,
                player.y + increase,
                player.z,
                false,
                player.horizontalCollision
            ))
        }
    }
}