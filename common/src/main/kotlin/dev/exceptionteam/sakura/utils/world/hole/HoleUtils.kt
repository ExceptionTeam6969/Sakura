package dev.exceptionteam.sakura.utils.world.hole

import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object HoleUtils {

    fun getHoleType(pos: BlockPos): HoleInfo? {
        var box = AABB(pos)
        if (isSingleHole(pos)) return HoleInfo(box, HoleType.SINGLE)
        getDoubleHole(pos)?.let {
            box = box.minmax(AABB(it))
            return HoleInfo(box, HoleType.DOUBLE)
        }
        return null
    }

    fun isSingleHole(pos: BlockPos): Boolean {
        if (!isAir(pos) || !isAir(pos.above()) || isAir(pos.below())) return false
        Direction.entries
            .filter { it != Direction.DOWN && it != Direction.UP }
            .forEach {
                val position = pos.relative(it)
                val blockState = position.blockState ?: return@forEach
                val block = blockState.block
                if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK) return false
            }
        return true
    }

    fun getDoubleHole(pos: BlockPos): BlockPos? {
        var doublePos: BlockPos? = null

        if (!isAir(pos) || !isAir(pos.above()) || isAir(pos.below())) {
            return null
        }

        for (direction in Direction.entries.filter { it != Direction.DOWN && it != Direction.UP }) {
            val offset = pos.relative(direction)
            val offsetState = offset.blockState ?: return null
            when {
                isAir(offset) -> {
                    if (doublePos != null) return null
                    if (!isAir(offset.above()) || isAir(offset.below())) return null
                    for (facing in Direction.entries.filter {
                        it != Direction.DOWN &&
                                it != Direction.UP &&
                                it != direction.opposite
                    }) {
                        val facingPos = offset.relative(facing)
                        val facingState = facingPos.blockState ?: return null
                        if (!holeBlocks.contains(facingState.block)) return null
                    }
                    doublePos = offset
                }
                !holeBlocks.contains(offsetState.block) -> return null
            }
        }
        return doublePos
    }

    private fun isAir(pos: BlockPos): Boolean {
        val blockState = pos.blockState ?: return false
        return blockState.block == Blocks.AIR
    }

    private val holeBlocks = arrayListOf(
        Blocks.BEDROCK,
        Blocks.OBSIDIAN,
        Blocks.RESPAWN_ANCHOR,
        Blocks.CRYING_OBSIDIAN,
        Blocks.NETHERITE_BLOCK
    )

    data class HoleInfo(val box: AABB, val holeType: HoleType)

}