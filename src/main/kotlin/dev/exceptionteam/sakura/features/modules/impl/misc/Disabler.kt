package dev.exceptionteam.sakura.features.modules.impl.misc

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

object Disabler: Module(
    name = "disabler",
    category = Category.MISC,
) {
    private val noConClick by setting("no-container-click", false)
    private val noMovePlayer by setting("no-move-player", false)
    private val noEntityMotion by setting("no-entity-motion", false)

    init {
        nonNullListener<PacketEvents.Receive> { e ->
            when (e.packet) {
                // C0f
                is ServerboundContainerClickPacket -> {
                    if (noConClick) e.cancel()
                }
                is ServerboundMovePlayerPacket -> {
                    if (noMovePlayer) e.cancel()
                }
                is ClientboundSetEntityMotionPacket -> {
                    if (noEntityMotion) e.cancel()
                }
            }
        }

        nonNullListener<TickEvents.Update> {
            if(noMovePlayer) {
                connection.send(
                    ServerboundMovePlayerPacket.PosRot(
                        player.x, player.y, player.z,
                        player.yRot, player.xRot,
                        player.onGround(),
                    )
                )
            }
        }
    }
}