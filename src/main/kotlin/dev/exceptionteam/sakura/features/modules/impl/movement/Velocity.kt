package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.utils.entity.PlayerUtils.isInBlock
import dev.exceptionteam.sakura.utils.interfaces.TranslationEnum
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket

object Velocity: Module(
    name = "velocity",
    category = Category.MOVEMENT
) {

    val noPush by setting("no-push", true)
    private val mode by setting("mode", Mode.NORMAL)

    init {

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

                is ExplosionS2CPacket -> {
                    when (mode) {
                        Mode.NORMAL -> {
                            val velocity = e.packet.playerKnockback.get()
                            velocity.x = 0.0
                            velocity.y = 0.0
                            velocity.z = 0.0
                        }

                        Mode.WALL -> {
                            if (isInBlock()) {
                                val velocity = e.packet.playerKnockback.get()
                                velocity.x = 0.0
                                velocity.y = 0.0
                                velocity.z = 0.0
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