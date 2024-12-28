package dev.exceptionteam.sakura.utils.world

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.utils.world.WorldUtils.blockState
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

object BlockUtils {

    fun NonNullContext.getNeighbourSide(pos: BlockPos): Direction? {
        Direction.entries.filter {
            val state = pos.relative(it).blockState ?: return@filter false
            !state.isAir
        }.forEach { return it }
        return null
    }

}