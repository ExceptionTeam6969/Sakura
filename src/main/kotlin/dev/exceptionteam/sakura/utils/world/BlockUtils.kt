package dev.exceptionteam.sakura.utils.world

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec3

object BlockUtils {

    fun NonNullContext.getNeighbourSide(pos: BlockPos): Direction? {
        Direction.entries.filter {
            val state = pos.relative(it).blockState ?: return@filter false
            !state.isAir
        }.forEach { return it }
        return null
    }

    fun getVecPos(pos: BlockPos, dir: Direction? = null): Vec3 =
        dir?.let { pos.bottomCenter.add(dir.stepX * 0.5, dir.stepY * 0.5, dir.stepZ * 0.5) } ?: pos.bottomCenter

}