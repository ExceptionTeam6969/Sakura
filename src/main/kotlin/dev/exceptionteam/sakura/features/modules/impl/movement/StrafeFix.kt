package dev.exceptionteam.sakura.features.modules.impl.movement

import dev.exceptionteam.sakura.events.impl.MovementInputEvent
import dev.exceptionteam.sakura.events.impl.PlayerJumpEvent
import dev.exceptionteam.sakura.events.impl.PlayerVelocityStrafeEvent
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.RotationManager.rotationInfo
import net.minecraft.util.math.MathHelper
import kotlin.math.abs

object StrafeFix: Module(
    name = "strafe-fix",
    category = Category.MOVEMENT,
) {

    init {

        nonNullListener<MovementInputEvent> { event ->
            rotationInfo?.let { inf ->
                val yaw = inf.yaw
                val forward: Float = event.forward
                val strafe: Float = event.strafe

                val angle = MathHelper.wrapDegrees(Math.toDegrees(direction(player.yaw, forward, strafe)))

                if (forward == 0f && strafe == 0f) {
                    return@nonNullListener
                }

                var closestForward = 0f
                var closestStrafe = 0f
                var closestDifference = Float.MAX_VALUE

                for (predictedForward in -1..1) {
                    for (predictedStrafe in -1..1) {
                        if (predictedStrafe == 0 && predictedForward == 0) continue

                        val predictedAngle = MathHelper.wrapDegrees(Math.toDegrees(
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

        nonNullListener<PlayerVelocityStrafeEvent> { event ->
            rotationInfo?.let { event.yaw = it.yaw }
        }

        nonNullListener<PlayerJumpEvent> { event ->
            rotationInfo?.let { event.yaw = it.yaw }
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

}