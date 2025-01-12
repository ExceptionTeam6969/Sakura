package dev.exceptionteam.sakura.utils.world

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.Wrapper.world
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.boss.enderdragon.EndCrystal
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object BlockUtils {

    fun NonNullContext.getNeighbourSide(pos: BlockPos): Direction? {
        Direction.entries.filter {
            val state = pos.relative(it).blockState ?: return@filter false
            !state.isAir
        }.forEach { return it }
        return null
    }



    fun NonNullContext.canBreak(pos: BlockPos): Boolean {
        val blockState: BlockState = world.getBlockState(pos)
        val block = blockState.block
        return block.defaultDestroyTime() !== (-1).toFloat()
    }

    fun getVecPos(pos: BlockPos, dir: Direction? = null): Vec3 =
        dir?.let { pos.center.add(dir.stepX * 0.5, dir.stepY * 0.5, dir.stepZ * 0.5) } ?: pos.center

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
            blockPos.x.toDouble() - 0.3,
            blockPos.y + 1.0,
            blockPos.z.toDouble() - 0.3,
            blockPos.x + 1.6,
            blockPos.y + 3.0,
            blockPos.z + 1.6
        )

        val upBox = AABB(
            blockPos.above().x.toDouble() - 0.3,
            blockPos.above().y + 1.0,
            blockPos.above().z.toDouble() - 0.3,
            blockPos.above().x + 1.6,
            blockPos.above().y + 3.0,
            blockPos.above().z + 1.6
        )

        for (entity in world.entitiesForRendering()) {
            if (entity is EndCrystal) continue
            if (entity.boundingBox.intersects(box)) return false
            if (entity.boundingBox.intersects(upBox)) return false
        }

        return base !is FireBlock
    }

}