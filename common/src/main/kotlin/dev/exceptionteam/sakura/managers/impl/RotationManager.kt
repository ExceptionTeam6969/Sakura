package dev.exceptionteam.sakura.managers.impl

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.PacketEvents
import dev.exceptionteam.sakura.events.impl.PlayerMotionEvent
import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.impl.client.Rotations
import dev.exceptionteam.sakura.mixins.core.packet.ServerboundMovePlayerPacketAccessor
import dev.exceptionteam.sakura.utils.math.vector.Vec2f
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

object RotationManager {

    init {
        nonNullListener<PlayerMotionEvent.Pre>(alwaysListening = true, priority = Int.MIN_VALUE) {
            rotationInfo?.let { info ->
                if (Rotations.packetRotation)
                    connection.send(ServerboundMovePlayerPacket.Rot(
                        info.yaw, info.pitch, player.onGround(), player.isShiftKeyDown
                    ))

                info.func()
            }
        }

        nonNullListener<PlayerMotionEvent>(alwaysListening = true, priority = Int.MAX_VALUE) { e ->
            if (Rotations.packetRotation) return@nonNullListener

            rotationInfo?.let {
                e.yaw = it.yaw
                e.pitch = it.pitch
            }
        }

        nonNullListener<TickEvents.Pre>(alwaysListening = true, priority = Int.MAX_VALUE) {
            rotationInfo = null
        }

    }

    var rotationInfo: RotationInfo? = null; private set

    val NonNullContext.rotationYaw: Float get() = rotationInfo?.yaw ?: player.yRot
    val NonNullContext.rotationPitch: Float get() = rotationInfo?.pitch ?: player.xRot

    /**
     * Add a rotation to the rotation manager.
     */
    fun NonNullContext.addRotation(
        yaw: Float, pitch: Float, priority: Int,
        shouldRotate: Boolean = true, func: () -> Unit = { }
    ) = RotationManager.addRotation(yaw, pitch, priority, shouldRotate, func)

    fun NonNullContext.addRotation(
        rotation: Vec2f, priority: Int,
        shouldRotate: Boolean = true, func: () -> Unit = { }
    ) = RotationManager.addRotation(rotation.x, rotation.y, priority, shouldRotate, func)

    /**
     * Add a rotation to the rotation manager.
     * @param yaw The yaw rotation to add.
     * @param pitch The pitch rotation to add.
     * @param priority The priority of the rotation.
     * @param shouldRotate If the rotation should be added or not.
     * @param func The function to execute after the rotation is added.
     */
    fun addRotation(
        yaw: Float, pitch: Float, priority: Int,
        shouldRotate: Boolean = true, func: () -> Unit = { }
    ) {
        if (!shouldRotate) {
            func()
            return
        }

        rotationInfo?.let {
            if (priority > it.priority) {
                rotationInfo = RotationInfo(yaw, pitch, priority, func)
            }
            return
        }

        rotationInfo = RotationInfo(yaw, pitch, priority, func)
    }

    data class RotationInfo(
        val yaw: Float,
        val pitch: Float,
        val priority: Int,
        val func: () -> Unit,
    )

}