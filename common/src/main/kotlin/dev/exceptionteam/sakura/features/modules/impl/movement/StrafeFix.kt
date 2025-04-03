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
import kotlin.math.*

object StrafeFix: Module(
    name = "strafe-fix",
    category = Category.MOVEMENT,
) {

    private var prevYaw = 0f
    private var prevPitch = 0f

    private val direction by setting("direction", true)

    init {

        nonNullListener<PlayerUpdateEvents.AiStepUpdate> {
            if (!direction) return@nonNullListener
            if (Rotations.packetRotation) return@nonNullListener

            fun wrapDegrees(angle: Double) = ((angle % 360.0) + 540.0) % 360.0 - 180.0

            fun computeDirection(baseYaw: Double, forward: Float, strafe: Float) =
                atan2(strafe.toDouble(), forward.toDouble()) + Math.toRadians(baseYaw)

            rotationInfo?.let {
                val (forwardInput, strafeInput) = player.input.run { forwardImpulse to leftImpulse }
                if (forwardInput == 0f && strafeInput == 0f) return@let

                val baseYaw = mc.player?.yRot ?: return@let
                val currentAngle =
                    wrapDegrees(Math.toDegrees(computeDirection(baseYaw.toDouble(), forwardInput, strafeInput)))

                var bestForward = 0f
                var bestStrafe = 0f
                var bestDifference = Float.MAX_VALUE

                for (f in -1..1) {
                    for (s in -1..1) {
                        if (f == 0 && s == 0) continue
                        val predictedAngle = wrapDegrees(Math.toDegrees(computeDirection(baseYaw.toDouble(), f.toFloat(), s.toFloat())))
                        val difference = abs(currentAngle - predictedAngle)
                        if (difference < bestDifference) {
                            bestDifference = difference.toFloat()
                            bestForward = f.toFloat()
                            bestStrafe = s.toFloat()
                        }
                    }
                }

                player.input.forwardImpulse = bestForward
                player.input.leftImpulse = bestStrafe
            }
        }

        nonNullListener<PlayerVelocityStrafeEvents.Pre> {
            if (Rotations.packetRotation) return@nonNullListener
            saveState()
        }

        nonNullListener<PlayerVelocityStrafeEvents.Post> {
            if (Rotations.packetRotation) return@nonNullListener
            restoreState()
        }

        nonNullListener<PlayerJumpEvents.Pre> {
            if (Rotations.packetRotation) return@nonNullListener
            saveState()
        }

        nonNullListener<PlayerJumpEvents.Post> {
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