package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.PlayerJumpEvents
import dev.exceptionteam.sakura.events.impl.PlayerUpdateEvents
import dev.exceptionteam.sakura.events.impl.PlayerVelocityStrafeEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.features.modules.impl.client.Rotations
import dev.exceptionteam.sakura.managers.impl.RotationManager.rotationInfo
import dev.exceptionteam.sakura.utils.math.toRadians
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

object StrafeFix: Module(
    name = "strafe-fix",
    category = Category.MOVEMENT,
) {

    private var prevYaw = 0f
    private var prevPitch = 0f

    private val direction by setting("direction", true)

    init {

        // fixme: It causes Simulation vl in the latest Grim server
        nonNullListener<PlayerUpdateEvents.AiStepUpdate> {
            if (!direction) return@nonNullListener
            if (Rotations.packetRotation) return@nonNullListener

            rotationInfo?.let { inf ->
                val movementForward = player.input.forwardImpulse
                val movementSideways = player.input.leftImpulse
                val delta = (player.yRot - inf.yaw).toRadians()
                val cos = cos(delta)
                val sin = sin(delta)
                player.input.leftImpulse = round(movementSideways * cos - movementForward * sin)
                player.input.forwardImpulse = round(movementForward * cos + movementSideways * sin)
            }
        }

        nonNullListener<PlayerVelocityStrafeEvents.Pre> { event ->
            if (Rotations.packetRotation) return@nonNullListener
            saveState()
        }

        nonNullListener<PlayerVelocityStrafeEvents.Post> { event ->
            if (Rotations.packetRotation) return@nonNullListener
            restoreState()
        }

        nonNullListener<PlayerJumpEvents.Pre> { event ->
            if (Rotations.packetRotation) return@nonNullListener
            saveState()
        }

        nonNullListener<PlayerJumpEvents.Post> { event ->
            if (Rotations.packetRotation) return@nonNullListener
            restoreState()
        }

    }

    private fun NonNullContext.saveState() {
        rotationInfo?.let {
            prevYaw = player.yRot
            player.yRot = it.yaw

            prevPitch = player.xRot
            player.xRot = it.pitch
        }
    }

    private fun NonNullContext.restoreState() {
        rotationInfo?.let {
            player.yRot = prevYaw
            player.xRot = prevPitch
        }
    }

}