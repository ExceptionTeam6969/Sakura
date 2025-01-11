/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package dev.exceptionteam.sakura.utils.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.math.MathUtils.lerp
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.Difficulty
import net.minecraft.world.damagesource.CombatRules
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

object DamageCalculator {

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

    private fun NonNullContext.raycastFactory(context: ExposureRaycastContext, pos: BlockPos): BlockHitResult? {
        val blockState = pos.blockState ?: return null
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
        val modDistance = targetPos.distanceTo(explosionPos)
        if (modDistance > power) return 0.0f

        val exposure = getExposure(explosionPos, targetAABB)
        val impact = (1 - (modDistance / power)) * exposure
        val damage = ((impact * impact + impact) / 2 * 7 * 12 + 1).toFloat()

        return calculateReductions(damage, target, world.damageSources().explosion(null))
    }

    private fun NonNullContext.getExposure(
        source: Vec3,
        box: AABB,
    ): Float {
        val xDiff: Double = box.maxX - box.minX
        val yDiff: Double = box.maxY - box.minY
        val zDiff: Double = box.maxZ - box.minZ

        var xStep = 1 / (xDiff * 2 + 1)
        var yStep = 1 / (yDiff * 2 + 1)
        var zStep = 1 / (zDiff * 2 + 1)

        if (xStep > 0 && yStep > 0 && zStep > 0) {
            var misses = 0
            var hits = 0

            val xOffset = (1 - floor(1 / xStep) * xStep) * 0.5
            val zOffset = (1 - floor(1 / zStep) * zStep) * 0.5

            xStep *= xDiff
            yStep *= yDiff
            zStep *= zDiff

            val startX: Double = box.minX + xOffset
            val startY: Double = box.minY
            val startZ: Double = box.minZ + zOffset
            val endX: Double = box.maxX + xOffset
            val endY: Double = box.maxY
            val endZ: Double = box.maxZ + zOffset

            var x = startX
            while (x <= endX) {
                var y = startY
                while (y <= endY) {
                    var z = startZ
                    while (z <= endZ) {
                        val position = Vec3(x, y, z)

                        if (raycast(ExposureRaycastContext(position, source)) == null) misses++

                        hits++
                        z += zStep
                    }
                    y += yStep
                }
                x += xStep
            }

            return misses.toFloat() / hits
        }

        return 0f
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

        val armor = entity.getAttributeValue(Attributes.ARMOR)

        // Armor reduction
        damage = CombatRules.getDamageAfterAbsorb(
            entity,
            damage,
            damageSource,
            armor.toFloat(),
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
            val lvl: Int = resistance.amplifier + 1
            damage *= (1 - (lvl * 0.2f))
        }

        return max(damage.toDouble(), 0.0).toFloat()
    }

    private fun protectionReduction(player: LivingEntity, damage: Float, source: DamageSource): Float {
        return CombatRules.getDamageAfterMagicAbsorb(damage, 0f)
    }

    private fun NonNullContext.raycast(
        context: ExposureRaycastContext,
    ): BlockHitResult? {
        return raycast(context.start, context.end, context)
    }

    data class ExposureRaycastContext(
        val start: Vec3,
        val end: Vec3,
    )

    private fun NonNullContext.raycast(
        start: Vec3,
        end: Vec3,
        context: ExposureRaycastContext,
    ): BlockHitResult? {
        if (start == end) {
            return null
        } else {
            val d = lerp(-1.0E-7, end.x, start.x)
            val e = lerp(-1.0E-7, end.y, start.y)
            val f = lerp(-1.0E-7, end.z, start.z)
            val g = lerp(-1.0E-7, start.x, end.x)
            val h = lerp(-1.0E-7, start.y, end.y)
            val i = lerp(-1.0E-7, start.z, end.z)
            var j = floor(g).toInt()
            var k = floor(h).toInt()
            var l = floor(i).toInt()
            var mutable = BlockPos(j, k, l)
            val obj: BlockHitResult? = raycastFactory(context, mutable)
            if (obj != null) {
                return obj
            } else {
                val m = d - g
                val n = e - h
                val o = f - i
                val p: Int = Mth.sign(m)
                val q: Int = Mth.sign(n)
                val r: Int = Mth.sign(o)
                val s = if (p == 0) Double.MAX_VALUE else p.toDouble() / m
                val t = if (q == 0) Double.MAX_VALUE else q.toDouble() / n
                val u = if (r == 0) Double.MAX_VALUE else r.toDouble() / o
                var v: Double = s * (if (p > 0) 1.0 - Mth.frac(g) else Mth.frac(g))
                var w: Double = t * (if (q > 0) 1.0 - Mth.frac(h) else Mth.frac(h))
                var x: Double = u * (if (r > 0) 1.0 - Mth.frac(i) else Mth.frac(i))

                while (v <= 1.0 || w <= 1.0 || x <= 1.0) {
                    if (v < w) {
                        if (v < x) {
                            j += p
                            v += s
                        } else {
                            l += r
                            x += u
                        }
                    } else if (w < x) {
                        k += q
                        w += t
                    } else {
                        l += r
                        x += u
                    }

                    mutable = BlockPos(j, k, l)
                    val object2: BlockHitResult? = raycastFactory(context, mutable)
                    if (object2 != null) {
                        return object2
                    }
                }

                return null
            }
        }
    }

}