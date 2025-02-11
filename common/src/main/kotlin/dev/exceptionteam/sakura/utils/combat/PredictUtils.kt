package dev.exceptionteam.sakura.utils.combat

import dev.exceptionteam.sakura.events.NonNullContext
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object PredictUtils {

    /**
     * Predicts the entity's motion for the given number of ticks.
     * @return The offset vector of the predicted motion.
     */
    fun NonNullContext.predictMotion(entity: Entity, ticks: Int): Vec3 {
        val motionX = (entity.x - entity.xOld).coerceIn(-0.6, 0.6)
        val motionY = (entity.y - entity.yOld).coerceIn(-0.5, 0.5)
        val motionZ = (entity.z - entity.zOld).coerceIn(-0.6, 0.6)

        val entityBox = entity.boundingBox
        var targetBox = entityBox

        for (tick in 0..ticks) {
            targetBox =
                canMove(targetBox, motionX, motionY, motionZ)
                    ?: canMove(targetBox, motionX, 0.0, motionZ)
                    ?: canMove(targetBox, 0.0, motionY, 0.0)
                    ?: break
        }

        val offsetX = targetBox.minX - entityBox.minX
        val offsetY = targetBox.minY - entityBox.minY
        val offsetZ = targetBox.minZ - entityBox.minZ
        return Vec3(offsetX, offsetY, offsetZ)
    }

    private fun NonNullContext.canMove(box: AABB, x: Double, y: Double, z: Double): AABB? {
        return box.move(x, y, z).takeIf { world.noCollision(it) }
    }

}