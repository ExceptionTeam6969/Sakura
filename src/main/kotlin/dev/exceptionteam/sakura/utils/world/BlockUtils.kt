package dev.exceptionteam.sakura.utils.world

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.collections.EnumSet
import dev.exceptionteam.sakura.utils.math.distanceSqTo
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.EnumSet

object BlockUtils {

    /**
     * Get the neighbour side of the block at the given position.
     */
    fun NonNullContext.getNeighbourSide(pos: BlockPos): Direction? {
        Direction.entries
            .filter { pos.relative(it).blockState?.block == Blocks.AIR }
            .let { return it.firstOrNull() }
        return null
    }

    /**
     * Get the best side that can be reached by the player.
     * Player can raytrace to the block and get the side that is the closest to the player.
     */
    fun NonNullContext.getBestSide(pos: BlockPos): Direction? =
        getVisibleSides(pos)
            .filter { world.getBlockState(pos.relative(it)).block == Blocks.AIR }
            .minByOrNull { player.eyePosition.distanceSqTo(getVecPos(pos, it)) }

    fun NonNullContext.canBreak(pos: BlockPos): Boolean {
        val blockState: BlockState = world.getBlockState(pos)
        val block = blockState.block
        return block.defaultDestroyTime() != (-1).toFloat()
    }

    fun getVecPos(pos: BlockPos, dir: Direction? = null): Vec3 =
        dir?.let { pos.center.add(dir.stepX * 0.5, dir.stepY * 0.5, dir.stepZ * 0.5) } ?: pos.center

    fun NonNullContext.getVisibleSides(pos: BlockPos, assumeAirAsFullBox: Boolean = false): Set<Direction> {
        val visibleSides = EnumSet<Direction>()

        val eyePos = player.eyePosition
        val blockCenter = pos.center
        val blockState = world.getBlockState(pos)
        val isFullBox = assumeAirAsFullBox && blockState.block == Blocks.AIR || blockState.isCollisionShapeFullBlock(world, pos)

        return visibleSides
            .checkAxis(eyePos.x - blockCenter.x, Direction.WEST, Direction.EAST, !isFullBox)
            .checkAxis(eyePos.y - blockCenter.y, Direction.DOWN, Direction.UP, true)
            .checkAxis(eyePos.z - blockCenter.z, Direction.NORTH, Direction.SOUTH, !isFullBox)
    }

    private fun EnumSet<Direction>.checkAxis(
        diff: Double,
        negativeSide: Direction,
        positiveSide: Direction,
        bothIfInRange: Boolean
    ) =
        this.apply {
            when {
                diff < -0.5 -> {
                    add(negativeSide)
                }
                diff > 0.5 -> {
                    add(positiveSide)
                }
                else -> {
                    if (bothIfInRange) {
                        add(negativeSide)
                        add(positiveSide)
                    }
                }
            }
        }

    /**
     * Check if the given block can be placed a crystal.
     * @param blockPos The position of the block to check.
     * @param oldPlace 1.12 and below
     * @return True if the block can be placed a crystal, false otherwise.
     */
    fun NonNullContext.canPlaceCrystal(
        blockPos: BlockPos, oldPlace: Boolean = false
    ): Boolean {
        val boost = blockPos.offset(0, 1, 0)
        val base = world.getBlockState(blockPos).block
        val b1 = world.getBlockState(boost).block

        if (base != Blocks.BEDROCK && base != Blocks.OBSIDIAN) {
            return false
        }

        if (b1 != Blocks.AIR) return false

        if (!blockPos.above(2).blockState?.isAir!! && oldPlace) return false

        val box = AABB(
            blockPos.x.toDouble(),
            blockPos.y + 1.0,
            blockPos.z.toDouble(),
            blockPos.x + 1.0,
            blockPos.y + 3.0,
            blockPos.z + 1.0
        )

        val upBox = AABB(
            blockPos.above().x.toDouble(),
            blockPos.above().y + 1.0,
            blockPos.above().z.toDouble(),
            blockPos.above().x + 1.0,
            blockPos.above().y + 3.0,
            blockPos.above().z + 1.0
        )

        for (entity in world.entitiesForRendering()) {
            if (entity.boundingBox.intersects(box)) return false
            if (entity.boundingBox.intersects(upBox) && oldPlace) return false
        }

        return base !is FireBlock
    }

}