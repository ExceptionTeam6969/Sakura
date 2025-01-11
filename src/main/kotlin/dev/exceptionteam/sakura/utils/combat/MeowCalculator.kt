package dev.exceptionteam.sakura.utils.combat


import dev.exceptionteam.sakura.events.NonNullContext
import net.minecraft.core.BlockPos
import net.minecraft.world.Difficulty
import net.minecraft.world.damagesource.CombatRules
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

object MeowCalculator {
    fun NonNullContext.anchorDamage(
        target: LivingEntity,
        targetPos: Vec3,
        targetAABB: AABB,
        anchorPos: Vec3,
    ): Float = explosionDamage(target, targetPos, targetAABB, anchorPos, 10.0f)

    fun NonNullContext.crystalDamage(
        target: LivingEntity,
        targetPos: Vec3,
        targetAABB: AABB,
        crystalPos: Vec3,
    ): Float = explosionDamage(target, targetPos, targetAABB, crystalPos, 12.0f)

    private fun NonNullContext.rayCatFactory(context: ExposureRayCatContext, pos: BlockPos): BlockHitResult? {
        val blockState = world.getBlockState(pos) ?: return null
        if (blockState.block.explosionResistance < 600) return null
        return blockState.getCollisionShape(world, pos).clip(context.start, context.end, pos)
    }

    private fun NonNullContext.explosionDamage(
        target: LivingEntity,
        targetPos: Vec3,
        targetAABB: AABB,
        explosionPos: Vec3,
        power: Float,
    ): Float {
        val distance = targetPos.distanceTo(explosionPos)
        if (distance > power) return 0.0f

        val exposure = getExposure(explosionPos, targetAABB)
        val impact = (1 - (distance / power)) * exposure
        val damage = ((impact * impact + impact) / 2 * 7 * 12 + 1).toFloat()

        return calculateReductions(damage, target, world.damageSources().explosion(null))
    }

    private fun NonNullContext.getExposure(
        source: Vec3,
        box: AABB,
    ): Float {
        val xDiff = box.maxX - box.minX
        val yDiff = box.maxY - box.minY
        val zDiff = box.maxZ - box.minZ

        val xStep = 1 / (xDiff * 2 + 1)
        val yStep = 1 / (yDiff * 2 + 1)
        val zStep = 1 / (zDiff * 2 + 1)

        if (xStep <= 0 || yStep <= 0 || zStep <= 0) return 0f

        val xOffset = (1 - floor(1 / xStep) * xStep) * 0.5
        val zOffset = (1 - floor(1 / zStep) * zStep) * 0.5

        val startX = box.minX + xOffset
        val startY = box.minY
        val startZ = box.minZ + zOffset
        val endX = box.maxX + xOffset
        val endY = box.maxY
        val endZ = box.maxZ + zOffset

        var misses = 0
        var hits = 0

        var x = startX
        while (x <= endX) {
            var y = startY
            while (y <= endY) {
                var z = startZ
                while (z <= endZ) {
                    val position = Vec3(x, y, z)
                    if (rayCat(ExposureRayCatContext(position, source)) == null) misses++
                    hits++
                    z += zStep
                }
                y += yStep
            }
            x += xStep
        }

        return misses.toFloat() / hits
    }

    private fun NonNullContext.calculateReductions(damage0: Float, entity: LivingEntity, damageSource: DamageSource): Float {
        var damage = damage0

        if (damageSource.scalesWithDifficulty()) {
            when (world.difficulty) {
                Difficulty.EASY -> damage = min((damage / 2 + 1).toDouble(), damage.toDouble()).toFloat()
                Difficulty.HARD -> damage *= 1.5f
                else -> {}
            }
        }

        val armor = entity.getAttributeValue(Attributes.ARMOR).toFloat()
        entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS).toFloat()

        // Armor reduction
        damage = CombatRules.getDamageAfterAbsorb(
            entity,
            damage,
            damageSource,
            armor,
            entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS).toFloat()
        )

        // Resistance reduction
        damage = resistanceReduction(entity, damage)

        // Protection reduction
        damage = protectionReduction(entity, damage, damageSource)

        return max(damage.toDouble(), 0.0).toFloat()
    }

    private fun resistanceReduction(player: LivingEntity, damage0: Float): Float {
        var damage = damage0
        val resistance = player.getEffect(MobEffects.DAMAGE_RESISTANCE)
        if (resistance != null) {
            val level = resistance.amplifier + 1
            damage *= (1 - (level * 0.2f))
        }

        return max(damage.toDouble(), 0.0).toFloat()
    }

    private fun protectionReduction(player: LivingEntity, damage: Float, source: DamageSource): Float {
        return CombatRules.getDamageAfterMagicAbsorb(damage, 0f)
    }

    private fun NonNullContext.rayCat(context: ExposureRayCatContext): BlockHitResult? {
        return rayCat(context.start, context.end, context)
    }

    private fun NonNullContext.rayCat(start: Vec3, end: Vec3, context: ExposureRayCatContext): BlockHitResult? {
        if (start == end) return null

        val dx = end.x - start.x
        val dy = end.y - start.y
        val dz = end.z - start.z

        var x = start.x
        var y = start.y
        var z = start.z

        val stepX = if (dx == 0f.toDouble()) Double.MAX_VALUE else (if (dx > 0) 1 else -1) / dx
        val stepY = if (dy == 0f.toDouble()) Double.MAX_VALUE else (if (dy > 0) 1 else -1) / dy
        val stepZ = if (dz == 0f.toDouble()) Double.MAX_VALUE else (if (dz > 0) 1 else -1) / dz

        var nextX = if (dx == 0f.toDouble()) Double.MAX_VALUE else (if (dx > 0) ceil(x) - x else x - floor(x)) * stepX
        var nextY = if (dy == 0f.toDouble()) Double.MAX_VALUE else (if (dy > 0) ceil(y) - y else y - floor(y)) * stepY
        var nextZ = if (dz == 0f.toDouble()) Double.MAX_VALUE else (if (dz > 0) ceil(z) - z else z - floor(z)) * stepZ

        val currentPos = BlockPos.MutableBlockPos(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())

        while (true) {
            val hitResult = rayCatFactory(context, currentPos)
            if (hitResult != null) return hitResult

            if (nextX < nextY && nextX < nextZ) {
                x += stepX
                currentPos.x += if (dx > 0) 1 else -1
                nextX += stepX
            } else if (nextY < nextZ) {
                y += stepY
                currentPos.y += if (dy > 0) 1 else -1
                nextY += stepY
            } else {
                z += stepZ
                currentPos.z += if (dz > 0) 1 else -1
                nextZ += stepZ
            }

            if (x < min(start.x, end.x) || x > max(start.x, end.x) ||
                y < min(start.y, end.y) || y > max(start.y, end.y) ||
                z < min(start.z, end.z) || z > max(start.z, end.z)) {
                break
            }
        }

        return null
    }

    data class ExposureRayCatContext(
        val start: Vec3,
        val end: Vec3,
    )


}


