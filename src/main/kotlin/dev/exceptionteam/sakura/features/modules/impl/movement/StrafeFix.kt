package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.events.impl.MovementInputEvent
import dev.exceptionteam.sakura.events.impl.PlayerJumpEvents
import dev.exceptionteam.sakura.events.impl.PlayerVelocityStrafeEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.features.modules.impl.client.Rotations
import dev.exceptionteam.sakura.managers.impl.RotationManager.rotationInfo
import net.minecraft.util.Mth
import kotlin.math.abs

object StrafeFix: Module(
    name = "strafe-fix",
    category = Category.MOVEMENT,
) {

    private var prevYaw = 0f
    private var prevPitch = 0f

    private val direction by setting("direction", true)

    init {

        nonNullListener<MovementInputEvent> { event ->
            if (!direction) return@nonNullListener
            if (Rotations.packetRotation) return@nonNullListener

            rotationInfo?.let { inf ->
                val yaw = inf.yaw
                val forward: Float = event.forward
                val strafe: Float = event.strafe

                val angle = Mth.wrapDegrees(Math.toDegrees(direction(player.yRot, forward, strafe)))

                if (forward == 0f && strafe == 0f) {
                    return@nonNullListener
                }

                var closestForward = 0f
                var closestStrafe = 0f
                var closestDifference = Float.MAX_VALUE

                for (predictedForward in -1..1) {
                    for (predictedStrafe in -1..1) {
                        if (predictedStrafe == 0 && predictedForward == 0) continue

                        val predictedAngle = Mth.wrapDegrees(Math.toDegrees(
                            direction(yaw, predictedForward.toFloat(), predictedStrafe.toFloat())))
                        val difference = abs(angle - predictedAngle)

                        if (difference < closestDifference) {
                            closestDifference = difference.toFloat()
                            closestForward = predictedForward.toFloat()
                            closestStrafe = predictedStrafe.toFloat()
                        }
                    }
                }

                event.forward = closestForward
                event.strafe = closestStrafe
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

    private fun direction(rotationYaw0: Float, moveForward: Float, moveStrafing: Float): Double {
        var rotationYaw = rotationYaw0
        if (moveForward < 0f) rotationYaw += 180f

        var forward = 1f

        if (moveForward < 0f) forward = -0.5f
        else if (moveForward > 0f) forward = 0.5f

        if (moveStrafing > 0f) rotationYaw -= 90f * forward
        if (moveStrafing < 0f) rotationYaw += 90f * forward

        return Math.toRadians(rotationYaw.toDouble())
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