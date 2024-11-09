package dev.exceptionteam.sakura.utils.math

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.math.vector.Vec2f
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2
import kotlin.math.hypot

object RotationUtils {

    /**
     * Normalize an angle to the range [-180, 180)
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
    fun getRotationTo(posFrom: Vec3d, posTo: Vec3d): Vec2f {
        return getRotationFromVec(posTo.subtract(posFrom))
    }

    /**
     * Get rotation from the player's position to another position vector
     */
    fun NonNullContext.getRotationTo(posTo: Vec3d): Vec2f =
        getRotationTo(player.pos, posTo)

    fun NonNullContext.getRotationTo(posTo: BlockPos): Vec2f =
        getRotationTo(player.pos, posTo.toCenterPos())

    fun getRotationFromVec(vec: Vec3d): Vec2f {
        val xz = hypot(vec.x, vec.z)
        val yaw = normalizeAngle(atan2(vec.z, vec.x).toDegree() - 90.0)
        val pitch = normalizeAngle(-atan2(vec.y, xz).toDegree())
        return Vec2f(yaw, pitch)
    }

}