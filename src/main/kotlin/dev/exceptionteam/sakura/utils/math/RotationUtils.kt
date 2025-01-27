package dev.exceptionteam.sakura.utils.math

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.math.vector.Vec2f
import dev.exceptionteam.sakura.utils.world.BlockUtils
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot

object RotationUtils {

    /**
     * Normalize an angle to the range [-180, 180]
     *
     * @param angleIn Input angle in degrees
     * @return Normalized angle in degrees
     */
    fun normalizeAngle(angleIn: Double): Double {
        var angle = angleIn
        angle %= 360.0
        if (angle >= 180.0) {
            angle -= 360.0
        }
        if (angle < -180.0) {
            angle += 360.0
        }
        return angle
    }

    /**
     * Get rotation from a position vector to another position vector
     *
     * @param posFrom Calculate rotation from this position vector
     * @param posTo Calculate rotation to this position vector
     * @return A vector containing the rotation in degrees (yaw, pitch)
     */
    fun NonNullContext.getRotationTo(posFrom: Vec3, posTo: Vec3): Vec2f {
        return getRotationFromVec(posTo.subtract(posFrom))
    }

    /**
     * Get rotation from the player's position to another position vector
     */
    fun NonNullContext.getRotationTo(posTo: Vec3): Vec2f =
        getRotationTo(player.position(), posTo)

    fun NonNullContext.getRotationTo(posTo: BlockPos, dir: Direction? = null): Vec2f {
        val pos = BlockUtils.getVecPos(posTo, dir)

        return getRotationTo(player.position(), pos)
    }

    fun NonNullContext.getRotationFromVec(vec: Vec3): Vec2f {
        val xz = hypot(vec.x, vec.z)
        val yaw0 = normalizeAngle(atan2(vec.z, vec.x).toDegree() - 90.0)
        val pitch = normalizeAngle(-atan2(vec.y, xz).toDegree())

        val yawDiff1 = abs((player.yRot.toInt() / 360 * 360) + yaw0 - player.yRot)
        val yawDiff2 = abs((player.yRot.toInt() / 360 * 360) + 360 + yaw0 - player.yRot)

        // Get the closest yaw angle to the player's yaw angle
        val yaw =
            if (yawDiff1 < yawDiff2) (player.yRot.toInt() / 360 * 360) + yaw0
            else (player.yRot.toInt() / 360 * 360) + 360 + yaw0

        return Vec2f(yaw, pitch)
    }

}