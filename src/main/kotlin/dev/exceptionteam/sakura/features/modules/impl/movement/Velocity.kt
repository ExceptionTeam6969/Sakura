package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.impl.TickEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.player.PlayerUtils.isInBlock
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

object Velocity: Module(
    name = "velocity",
    category = Category.MOVEMENT
) {

    val noPush by setting("no-push", true)
    private val mode by setting("mode", Mode.NORMAL)

    var shouldCancelExplosion = false; private set

    init {

        nonNullListener<TickEvent.Post> {
            shouldCancelExplosion = if (mode == Mode.WALL) isInBlock() else true
        }

        nonNullListener<PacketEvents.Receive> { e ->

            when (e.packet) {
                is EntityVelocityUpdateS2CPacket -> {
                    val velocity = e.packet
                    if (velocity.entityId == player.id) {

                        when (mode) {
                            Mode.NORMAL -> {
                                e.cancel()
                            }

                            Mode.WALL -> {
                                if (isInBlock()) e.cancel()
                            }
                        }
                    }
                }
            }

        }

    }

    private enum class Mode(override val key: CharSequence): TranslationEnum {
        NORMAL("normal"),
        WALL("wall")
    }

}