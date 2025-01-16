package dev.exceptionteam.sakura.utils.combat

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.features.modules.impl.client.CombatSettings
import dev.exceptionteam.sakura.graphics.general.DirectionMask
import dev.exceptionteam.sakura.utils.math.distanceSqTo
import dev.exceptionteam.sakura.utils.math.fastFloor
import dev.exceptionteam.sakura.utils.world.WorldUtils.checkBlockCollision
import net.minecraft.core.BlockPos
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.collections.indices
import kotlin.math.min

class DamageCalculation(
    private val context: NonNullContext,
    val entity: LivingEntity,
    val predictPos: Vec3
) {
    val currentPos = entity.position()
    val currentAABB = getBoundingAABB(entity, currentPos)
    val predictAABB = getBoundingAABB(entity, predictPos)
    val clipped = context.world.checkBlockCollision(currentAABB)

    private val predicting = currentPos.distanceSqTo(predictPos) > 0.01
    private val difficulty = context.world.difficulty
    private val reduction = DamageReduction(entity)

    private val exposureSample = ExposureSample.getExposureSample(entity.bbWidth, entity.bbHeight)
    private val samplePoints = exposureSample.offset(currentAABB.minX, currentAABB.minY, currentAABB.minZ)
    private val samplePointsPredict = exposureSample.offset(predictAABB.minX, predictAABB.minY, predictAABB.minZ)

    @Suppress("DEPRECATION")
    fun isResistant(blockState: BlockState) =
        !blockState.isSolid && blockState.block.explosionResistance >= 19.7

    fun calcDamage(
        crystalX: Double,
        crystalY: Double,
        crystalZ: Double,
        predict: Boolean,
        mutableBlockPos: BlockPos.MutableBlockPos,
    ) = calcDamage(crystalX, crystalY, crystalZ, predict, 6.0f, mutableBlockPos)

    fun calcDamage(
        pos: Vec3,
        predict: Boolean,
        mutableBlockPos: BlockPos.MutableBlockPos,
    ) = calcDamage(pos, predict, 6.0f, mutableBlockPos)

    fun calcDamage(
        pos: Vec3,
        predict: Boolean,
        size: Float,
        mutableBlockPos: BlockPos.MutableBlockPos
    ) = calcDamage(pos.x, pos.y, pos.z, predict, size, mutableBlockPos)

    fun calcDamage(
        crystalX: Double,
        crystalY: Double,
        crystalZ: Double,
        predict: Boolean,
        size: Float,
        mutableBlockPos: BlockPos.MutableBlockPos
    ) = calcDamage(crystalX, crystalY, crystalZ, predict, size, mutableBlockPos) { _, blockState, _ ->
        if (blockState.block != Blocks.AIR && isResistant(blockState)) {
            FastRayTraceAction.CALC
        } else {
            FastRayTraceAction.SKIP
        }
    }

    fun calcDamage(
        pos: Vec3,
        predict: Boolean,
        mutableBlockPos: BlockPos.MutableBlockPos,
        function: FastRayTraceFunction
    ) = calcDamage(pos, predict, 6.0f, mutableBlockPos, function)

    fun calcDamage(
        pos: Vec3,
        predict: Boolean,
        size: Float,
        mutableBlockPos: BlockPos.MutableBlockPos,
        function: FastRayTraceFunction
    ) = calcDamage(pos.x, pos.y, pos.z, predict, size, mutableBlockPos, function)

    fun calcDamage(
        crystalX: Double,
        crystalY: Double,
        crystalZ: Double,
        predict: Boolean,
        size: Float,
        mutableBlockPos: BlockPos.MutableBlockPos,
        function: FastRayTraceFunction
    ): Float {
        if (difficulty == Difficulty.PEACEFUL) return 0.0f

        context {
            val entityPos = if (predict) predictPos else currentPos
            var damage =
                if (crystalY - entityPos.y > exposureSample.maxY
                    && isResistant(
                        world.getBlockState(
                            mutableBlockPos.set(
                                crystalX.fastFloor(),
                                crystalY.fastFloor() - 1,
                                crystalZ.fastFloor()
                            )
                        )
                    )
                ) {
                    1.0f
                } else {
                    calcRawDamage(crystalX, crystalY, crystalZ, size, predict, mutableBlockPos, function)
                }

            damage = calcDifficultyDamage(damage)
            return reduction.calcDamage(damage, true)
        }
    }

    private fun calcDifficultyDamage(damage: Float) =
        if (entity is Player) {
            when (difficulty) {
                Difficulty.EASY -> {
                    min(damage * 0.5f + 1.0f, damage)
                }
                Difficulty.HARD -> {
                    damage * 1.5f
                }
                else -> {
                    damage
                }
            }
        } else {
            damage
        }

    private fun NonNullContext.calcRawDamage(
        crystalX: Double,
        crystalY: Double,
        crystalZ: Double,
        size: Float,
        predict: Boolean,
        mutableBlockPos: BlockPos.MutableBlockPos,
        function: FastRayTraceFunction
    ): Float {
        val entityPos = if (predict) predictPos else currentPos
        val doubleSize = size * 2.0f
        val scaledDist = entityPos.distanceTo(Vec3(crystalX, crystalY, crystalZ)).toFloat() / doubleSize
        if (scaledDist > 1.0f) return 0.0f

        val factor =
            (1.0f - scaledDist) * getExposureAmount(crystalX, crystalY, crystalZ, predict, mutableBlockPos, function)
        return kotlin.math.floor((factor * factor + factor) * doubleSize * 3.5f + 1.0f)
    }

    private fun NonNullContext.getExposureAmount(
        crystalX: Double,
        crystalY: Double,
        crystalZ: Double,
        predict: Boolean,
        mutableBlockPos: BlockPos.MutableBlockPos,
        function: FastRayTraceFunction
    ): Float {
        val box = if (predict) predictAABB else currentAABB
        if (!clipped && box.isInside(crystalX, crystalY, crystalZ)) return 1.0f

        val array = if (predict) samplePointsPredict else samplePoints
        return if (!CombatSettings.backSideSampling) {
            countSamplePointsOptimized(array, box, crystalX, crystalY, crystalZ, mutableBlockPos, function)
        } else {
            countSamplePoints(array, crystalX, crystalY, crystalZ, mutableBlockPos, function)
        }
    }

    private fun NonNullContext.countSamplePoints(
        samplePoints: Array<Vec3>,
        crystalX: Double,
        crystalY: Double,
        crystalZ: Double,
        blockPos: BlockPos.MutableBlockPos,
        function: FastRayTraceFunction
    ): Float {
        var count = 0

        for (i in samplePoints.indices) {
            val samplePoint = samplePoints[i]
            if (!world.fastRayTrace(samplePoint, crystalX, crystalY, crystalZ, 20, blockPos, function)) {
                count++
            }
        }

        return count.toFloat() / samplePoints.size
    }

    private fun NonNullContext.countSamplePointsOptimized(
        samplePoints: Array<Vec3>,
        box: AABB,
        crystalX: Double,
        crystalY: Double,
        crystalZ: Double,
        mutableBlockPos: BlockPos.MutableBlockPos,
        function: FastRayTraceFunction
    ): Float {
        var count = 0
        var total = 0

        val sideMask = getSideMask(box, crystalX, crystalY, crystalZ)
        for (i in samplePoints.indices) {
            val pointMask = exposureSample.getMask(i)
            if (sideMask and pointMask == 0x00) {
                continue
            }

            total++
            val samplePoint = samplePoints[i]
            if (!world.fastRayTrace(samplePoint, crystalX, crystalY, crystalZ, 20, mutableBlockPos, function)) {
                count++
            }
        }

        return count.toFloat() / total.toFloat()
    }

    private fun getSideMask(
        box: AABB,
        posX: Double,
        posY: Double,
        posZ: Double
    ): Int {
        var mask = 0x00

        if (posX < box.minX) {
            mask = DirectionMask.WEST
        } else if (posX > box.maxX) {
            mask = DirectionMask.EAST
        }

        if (posY < box.minY) {
            mask = mask or DirectionMask.DOWN
        } else if (posY > box.maxY) {
            mask = mask or DirectionMask.UP
        }

        if (posZ < box.minZ) {
            mask = mask or DirectionMask.NORTH
        } else if (posZ > box.maxZ) {
            mask = mask or DirectionMask.SOUTH
        }

        return mask
    }

    private fun AABB.isInside(
        x: Double,
        y: Double,
        z: Double
    ): Boolean {
        return x >= this.minX && x <= this.maxX
                && y >= this.minY && y <= this.maxY
                && z >= this.minZ && z <= this.maxZ
    }

    private fun getBoundingAABB(entity: LivingEntity, pos: Vec3): AABB {
        val halfWidth = min(entity.bbWidth.toDouble(), 2.0)
        val height = min(entity.bbHeight.toDouble(), 3.0)

        return AABB(
            pos.x - halfWidth, pos.y, pos.z - halfWidth,
            pos.x + halfWidth, pos.y + height, pos.z + halfWidth
        )
    }
}