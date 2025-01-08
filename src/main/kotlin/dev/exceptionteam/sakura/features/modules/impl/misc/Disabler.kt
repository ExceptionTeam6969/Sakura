package dev.exceptionteam.sakura.features.modules.impl.misc

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

object Disabler: Module(
    name = "Disabler",
    category = Category.MISC,
) {
    private val c0f by setting("C0fDisabler", false)
    private val C2S by setting("NoMoveC2S", false)
    private val MotionPacket by setting("NoMotionPacket", false)

    init {
        nonNullListener<PacketEvents.Receive> { e ->
            //C0f
            if (e.packet is ServerboundContainerClickPacket) {
               if(c0f) {
                   e.cancel()
               }
            }
            if (e.packet is ServerboundMovePlayerPacket) {
                if(C2S) {
                    e.cancel()
                }
            }
            if (e.packet is ClientboundSetEntityMotionPacket) {
                if(MotionPacket) {
                    e.cancel()
                }
            }

        }

        nonNullListener<TickEvent.Post> {
            if(C2S) {
                connection.send(
                    ServerboundMovePlayerPacket.PosRot(
                        player.x, player.y, player.z,
                        player.yRot, player.xRot,
                        player.onGround(),
                        player.horizontalCollision
                    )
                )
            }
        }
    }
}